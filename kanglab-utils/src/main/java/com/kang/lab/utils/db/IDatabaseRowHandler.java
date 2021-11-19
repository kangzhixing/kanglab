package com.kang.lab.utils.db;

import java.sql.*;

public interface IDatabaseRowHandler {

	void map(ResultSet resultSet) throws Exception;
}
