package com.example.viewer.config;

import com.example.viewer.services.interfaces.JGitProvider;
import com.example.viewer.services.jgit.JGitCommitInfo;
import com.example.viewer.services.jgit.JGitObjectProducer;
import com.example.viewer.services.jgit.JGitProviderImpl;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;

@Configuration
@AllArgsConstructor
public class BeanConfig {

    private final ApplicationContext appContext;

    @Bean
    @Scope("prototype")
    public JGitProvider jGitProvider(){
        return new JGitProviderImpl();
    }

    @Bean
    @Scope("prototype")
    public JGitCommitInfo jGitCommitInfo(){
        return new JGitCommitInfo(appContext);
    }

    @Bean
    @Scope("prototype")
    public JGitObjectProducer jGitObjectProducer() {
        return new JGitObjectProducer(appContext);
    }
}
