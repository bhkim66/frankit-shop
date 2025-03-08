package com.frankit.shop.global.redis.repository;

import com.frankit.shop.global.redis.entity.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisRepositoryTest {

    @Autowired
    private RedisRepository redisRepository;

    @DisplayName("redis에 값을 저장할 수 있다")
    @Test
    void saveValueInRedis() {
        //given
        Token token = Token.of("usertest123@naver.com", "refreshToken123", 10*1000L);
        Token save = redisRepository.save(token);

        //when
        Token selectedToken = redisRepository.findById(save.getKey()).orElse(null);

        //then
        assertThat(selectedToken)
                .extracting("key", "refreshToken")
                .containsExactlyInAnyOrder("usertest123@naver.com", "refreshToken123");
    }

    @DisplayName("redis에 원하는 값을 출력할 수 있다")
    @Test
    void selectValueInRedis() {
        //given
        Token token = Token.of("usertest123@naver.com", "refreshToken123", 10*1000L);
        redisRepository.save(token);

        Token token1 = Token.of("testerKim321", "refreshToken321", 10*1000L);
        redisRepository.save(token1);

        //when
        Token selectedToken = redisRepository.findById("testerKim321").orElse(null);

        //then
        assertThat(selectedToken)
                .extracting("key", "refreshToken")
                .containsExactlyInAnyOrder("testerKim321", "refreshToken321");
    }

    @DisplayName("유효시간이 지나면 값을 출력할 수 없다")
    @Test
    void expiredValueCanNotFoundValue() throws InterruptedException {
        //given
        Token token = Token.of("usertest123@naver.com", "refreshToken123", 1000L);
        redisRepository.save(token);

        // 유효시간 1초에 2초 쉬고 체크
        Thread.sleep(2 * 1000L);

        //when
        Token selectedToken = redisRepository.findById("usertest123@naver.com").orElse(null);
        //then
        assertThat(selectedToken).isNull();
    }


    @DisplayName("redis에 원하는 값을 삭제할 수 있다")
    @Test
    void deleteValueInRedis() {
        //given
        Token token = Token.of("usertest123@naver.com", "refreshToken123", 10*1000L);
        redisRepository.save(token);

        //when
        redisRepository.deleteById("usertest123@naver.com");
        Token selectedToken = redisRepository.findById("usertest123@naver.com").orElse(null);

        //then
        assertThat(selectedToken).isNull();
    }

}