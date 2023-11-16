package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnecter {
	public static Connection getConnection() {
		Connection connection = null;

//      연결에 필요한 정보
		String userName = "scott";
		String password = "1234";
		String url = "jdbc:oracle:thin:@localhost:1521:XE";

		try {
//         드라이버를 메모리에 할당
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(url, userName, password);
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("연결 정보 오류");
			e.printStackTrace();
		}
		return connection;
	}
	
}
