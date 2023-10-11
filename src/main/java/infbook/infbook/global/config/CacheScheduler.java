package infbook.infbook.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheScheduler {

    //1시간 단위로 베스트셀러 리스트 캐쉬 초기화 해서 캐시를 최신화.
    @Scheduled(fixedRate = 3600000 )
    @CacheEvict(value = "bestSellerCache")
    public void resetCache(){
    }
}
