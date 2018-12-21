package db;

public class KlFieldDescription
    {

        /// <summary>
        /// 数据库类型
        /// </summary>
        private String dbType ;

        /// <summary>
        /// 描述
        /// </summary>
        private String description ;

        /// <summary>
        /// 是否可为Null
        /// </summary>
        private Boolean isNullable ;

        /// <summary>
        /// 是否为自增列
        /// </summary>
        private Boolean isIdentity ;

        /// <summary>
        /// 主外键情况
        /// </summary>
        private String columnKey ;

        /// <summary>
        /// 长度
        /// </summary>
        private Integer length ;

        /// <summary>
        /// 字段名
        /// </summary>
        private String name ;

        /// <summary>
        /// 字段名(去除下划线)
        /// </summary>
        private String simpleName;

        public String getDbType() {
            return dbType;
        }

        public void setDbType(String dbType) {
            this.dbType = dbType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Boolean getIsNullable() {
            return isNullable;
        }

        public void setIsNullable(Boolean nullable) {
            isNullable = nullable;
        }

        public Boolean getIsIdentity() {
            return isIdentity;
        }

        public void setIsIdentity(Boolean identity) {
            isIdentity = identity;
        }

        public String getColumnKey() {
            return columnKey;
        }

        public void setColumnKey(String columnKey) {
            this.columnKey = columnKey;
        }

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSimpleName() {
            return simpleName;
        }

        public void setSimpleName(String simpleName) {
            this.simpleName = simpleName;
        }
    }