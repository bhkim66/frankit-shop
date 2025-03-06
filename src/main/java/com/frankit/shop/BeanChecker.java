package com.frankit.shop;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanChecker implements ApplicationRunner {
        private final ApplicationContext applicationContext;

        public BeanChecker(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {
            boolean exists = applicationContext.containsBean("redisRepository");
            System.out.println("RedisRepository Bean 등록 여부: " + exists);
        }
}
