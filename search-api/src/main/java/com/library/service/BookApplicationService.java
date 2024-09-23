package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.entity.DailyStat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// 두 서비스를 하나로 묶기 위한 파사드
@RequiredArgsConstructor
@Service
public class BookApplicationService {
    private final BookQueryService bookQueryService;
    private final DailyStatCommandService dailyStatCommandService;

    public PageResult<SearchResponse> search(String query, int page, int size) {
        // 추후 개선 사항
        // 통계값 저장 후에 응답을 해주기 때문에 비효율적이다.
        PageResult<SearchResponse> response = bookQueryService.search(query, page, size);
        DailyStat dailyStat = new DailyStat(query, LocalDateTime.now());
        dailyStatCommandService.save(dailyStat);
        return response;
    }
}
