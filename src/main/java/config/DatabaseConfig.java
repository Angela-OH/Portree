package config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {
	
	/*
	@Value("${db.driver}")
	private String driverClassName;
	@Value("${db.url}")
	private String url;
	@Value("${db.username}")
	private String username;
	@Value("${db.password}")
	private String password;
	*/
	private String driverClassName = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost/portree?characterEncoding=utf8&enabledTLSProtocols=TLSv1.2";
	private String username = "portree";
	private String password = "portree";
	
	@Bean
	public DataSource dataSource() {
		DataSource ds = new DataSource();
		
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setInitialSize(2);
		ds.setMaxActive(10);
		ds.setTestWhileIdle(true);
		ds.setMinEvictableIdleTimeMillis(60000 * 3);
		ds.setTimeBetweenEvictionRunsMillis(10 * 1000);

		return ds;
	}
}
