package com.lcwd.electronic.store;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;

//@EnableWebMvc
@SpringBootApplication
//@EnableConfigServer
public class ElectronicStoreApplication extends SpringBootServletInitializer implements CommandLineRunner{

	@Value("${normal.role.id}")
    private String role_normal_id;
	
    @Value("${admin.role.id}")
    private String role_admin_id;
    
    @Autowired
    private RoleRepository repository;
    
    @Autowired
    private UserRepository userRepository;
    
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(ElectronicStoreApplication.class);
	}



	@Override
	public void run(String... args) throws Exception {
		System.out.println("Electronic Store started...!");
		
//		Role role_admin = Role.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
//        Role role_normal = Role.builder().roleId(role_normal_id).roleName("ROLE_NORMAL").build();
//
//
//        User adminUser = User.builder()
//                .name("admin")
//                .email("admin@gmail.com")
//                .password(passwordEncoder.encode("admin123"))
//                .gender("Male")
//                .imageName("default.png")
//                .roles(Set.of(role_admin, role_normal))
//                .userId(UUID.randomUUID().toString())
//                .about("I am admin User")
//                .build();
//
//        User normalUser = User.builder()
//                .name("durgesh")
//                .email("durgesh@gmail.com")
//                .password(passwordEncoder.encode("durgesh123"))
//                .gender("Male")
//                .imageName("default.png")
//                .roles(Set.of(role_normal))
//                .userId(UUID.randomUUID().toString())
//                .about("I am Normal User")
//                .build();
//
//        repository.save(role_admin);
//        repository.save(role_normal);
//
//
//        userRepository.save(adminUser);
//        userRepository.save(normalUser);
	}

}
