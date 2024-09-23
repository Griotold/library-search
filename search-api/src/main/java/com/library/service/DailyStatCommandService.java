package com.library.service;

import com.library.entity.DailyStat;
import com.library.repository.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DailyStatCommandService {
    private final DailyStatRepository dailyStatRepository;

    @Transactional
    public void save(DailyStat dailyStat) {
        // 저장이 되어야 하는데 안되는 경우가 종종 생김.
        // 로그를 중간중간 심어 놓는 게 굉장히 중요하다.
        log.info("save dailyStat: {}", dailyStat);
        dailyStatRepository.save(dailyStat);
    }
}
