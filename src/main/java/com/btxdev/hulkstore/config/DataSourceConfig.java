package com.btxdev.hulkstore.config;

import com.btxdev.hulkstore.App;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;

@Configuration
public class DataSourceConfig {
    
    @Bean
    public DataSource getDataSource() {
        ApplicationHome home = new ApplicationHome(App.class);
        File databaseFile =  new File(home.getDir(),"data");
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:file:"+databaseFile.getAbsolutePath()+";DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;");
        dataSourceBuilder.username("SA");
        dataSourceBuilder.password("password");
        return dataSourceBuilder.build();
    }
}