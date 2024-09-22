package com.library.controller.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @ToString
@Setter // 세터가 있어야 바인딩이 됨
public class SearchRequest {
    // 50 자 이하만
    @NotBlank(message = "입력은 비어있을 수 없습니다.")
    @Size(max = 50, message = "query는 최대 50자를 초과할 수 없습니다.")
    private String query;
    // 1 ~ 10000
    @NotNull(message = "페이지 번호는 필수 입니다.")
    @Min(value = 1, message = "페이지번호는 1 이상이어야 합니다.")
    @Max(value = 10000, message = "페이지번호는 최대 10000 입니다.")
    private Integer page;
    // 1 ~ 50
    @NotNull(message = "페이지 사이즈는 필수 입니다.")
    @Min(value = 1, message = "페이지 사이즈는 1 이상이어야 합니다.")
    @Max(value = 50, message = "페이지 사이즈는 최대 50 입니다.")
    private Integer size;
}
