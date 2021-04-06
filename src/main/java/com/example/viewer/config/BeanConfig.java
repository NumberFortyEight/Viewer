package com.example.viewer.config;

import com.example.viewer.services.interfaces.JGitProvider;
import com.example.viewer.services.jgit.JGitCommitInfo;
import com.example.viewer.services.jgit.JGitObjectProducer;
import com.example.viewer.services.jgit.JGitProviderImpl;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BeanConfig {
    @Bean
    @Scope("prototype")
    public JGitProviderImpl jGitProvider(){
        return new JGitProviderImpl();
    }

    @Bean
    @Scope("prototype")
    public JGitCommitInfo jGitCommitInfo(String fullPath, JGitProvider jGitProvider){
        return new JGitCommitInfo(fullPath, jGitProvider);
    }
    @Bean
    @Scope("prototype")
    public JGitObjectProducer jGitObjectProducer(RevCommit revCommit, String fullPath, JGitProvider jGitProvider) {
        return new JGitObjectProducer(revCommit, fullPath, jGitProvider);
    }
}
