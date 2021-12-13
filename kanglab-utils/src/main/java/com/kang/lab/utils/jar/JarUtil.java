package com.kang.lab.utils.jar;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;
import org.eclipse.aether.util.repository.AuthenticationBuilder;

import java.io.File;
import java.util.List;

/**
 * jar包工具类
 *
 * @author kangzhixing
 * @date 2021-12-02 16:00:47
 */
public class JarUtil {

    public static final String DEFAULT_TARGET = "./target/repo";

    public static final String DEFAULT_REPO = "https://repo1.maven.org/maven2/";

    /**
     * 从指定maven地址下载指定jar包
     *
     * @param jarDownloadParams 相关信息
     * @throws ArtifactResolutionException
     * @throws DependencyCollectionException
     * @throws DependencyResolutionException
     */
    public static List<File> downLoad(JarDownloadParams jarDownloadParams) throws ArtifactResolutionException, DependencyCollectionException, DependencyResolutionException {
        String groupId = jarDownloadParams.getGroupId();
        String artifactId = jarDownloadParams.getArtifactId();
        String version = jarDownloadParams.getVersion();
        String repositoryUrl = jarDownloadParams.getRepository() == null ? DEFAULT_REPO : jarDownloadParams.getRepository();
        String target = jarDownloadParams.getTarget() == null ? DEFAULT_TARGET : jarDownloadParams.getTarget();
        String username = jarDownloadParams.getUsername();
        String password = jarDownloadParams.getPassword();
        RepositorySystem repoSystem = newRepositorySystem();
        RepositorySystemSession session = newSession(repoSystem, target);
        RemoteRepository central;
        if (username == null && password == null) {
            central = new RemoteRepository.Builder("central", "default", repositoryUrl).build();
        } else {
            Authentication authentication = new AuthenticationBuilder().addUsername(username).addPassword(password).build();
            central = new RemoteRepository.Builder("central", "default", repositoryUrl).setAuthentication(authentication).build();
        }
        // 下载一个jar包
        Artifact artifact = new DefaultArtifact(groupId + ":" + artifactId + ":" + version);
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.addRepository(central);
        artifactRequest.setArtifact(artifact);
        repoSystem.resolveArtifact(session, artifactRequest);
        // 下载该jar包及其所有依赖jar包
        Dependency dependency = new Dependency(new DefaultArtifact(groupId + ":" + artifactId + ":" + version), null);
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(dependency);
        collectRequest.addRepository(central);
        DependencyNode node = repoSystem.collectDependencies(session, collectRequest).getRoot();
        DependencyRequest dependencyRequest = new DependencyRequest();
        dependencyRequest.setRoot(node);
        repoSystem.resolveDependencies(session, dependencyRequest);
        PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
        node.accept(nlg);
        return nlg.getFiles();
    }

    private static RepositorySystemSession newSession(RepositorySystem system, String target) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository(target);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
        return session;
    }

    /**
     * 建立RepositorySystem
     */
    private static RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        return locator.getService(RepositorySystem.class);
    }
}
