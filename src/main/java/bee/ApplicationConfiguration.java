package bee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

@Configuration
@PropertySource("classpath:config.properties")
@EnableWebMvc
@ComponentScan(basePackages = "bee")
@EnableMongoRepositories(basePackages = "bee")

public class ApplicationConfiguration {
	
	@Autowired
	private Environment env;
	
	
	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception{
//		ServerAddress serverAddress = new ServerAddress(env.getProperty("host"), Integer.parseInt(env.getProperty("port")));
		
		ArrayList<ServerAddress> servers = new ArrayList<ServerAddress>();
		
		try {
			File file = new File("/tmp/mongos.txt");
			System.out.println("FILE:------" + file);
			FileReader fileReader = new FileReader(file);
			
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			
			while ((line = bufferedReader.readLine()) != null) {
				String serverIp = line.split(":")[0];
				System.out.println(line.split(":")[0]);
				int serverPort = Integer.parseInt(line.split(":")[1]);
				servers.add(new ServerAddress(serverIp, serverPort));
			}
			fileReader.close();
			
		}catch (IOException e) {
			e.printStackTrace();				
		}
		
		
//		MongoCredential mongocred = MongoCredential.createCredential(env.getProperty("uname"), env.getProperty("authDB"), env.getProperty("password").toCharArray());
//		
//		ArrayList<MongoCredential> credList = new ArrayList<MongoCredential>();
//		credList.add(mongocred);
		System.out.println("Testing lines-----"+ env.getProperty("database"));
		MongoClientOptions.Builder optionsBuilder = MongoClientOptions.builder();
		optionsBuilder.connectTimeout(20000);
		optionsBuilder.socketTimeout(2000);
		optionsBuilder.readPreference(ReadPreference.secondaryPreferred());
		
		
		MongoClientOptions options = optionsBuilder.build();
		
		MongoCredential mongocred = MongoCredential.createCredential(env.getProperty("uname"), env.getProperty("database"), env.getProperty("password").toCharArray());
		List<MongoCredential> credList = new ArrayList<MongoCredential>();
		credList.add(mongocred);
		
		MongoClient mongoClient = new MongoClient(servers, credList, options);
		System.out.println(env.getProperty("database"));
				
		return new SimpleMongoDbFactory(mongoClient, env.getProperty("database"));
	}
	@Bean
	public MongoTemplate mongoTemplate() throws Exception{
		
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		System.out.println("mongo templet==="+mongoTemplate.getDb().getName());
		
		return mongoTemplate;
		
	}

}
