package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

  default <T> PageResponse<T> fromSlice(Slice<T> slice) {
    return PageResponse.<T>builder()
        .content(slice.getContent())
        .number(slice.getNumber())
        .size(slice.getSize())
        .hasNext(slice.hasNext())
        .build();
  }

  // Page<Entity> → PageResponse<Dto>
  default <T, R> PageResponse<R> fromSlice(Slice<T> slice, List<R> items) {
    return PageResponse.<R>builder()
        .content(items)
        .number(slice.getNumber() + 1)
        .size(slice.getSize())
        .hasNext(slice.hasNext())
        .build();
  }

  // Page<Entity> → PageResponse<Entity>
  default <T> PageResponse<T> fromPage(Page<T> page) {
    return PageResponse.<T>builder()
        .content(page.getContent())
        .number(page.getNumber() + 1)
        .size(page.getSize())
        .hasNext(page.hasNext())
        .totalElements(page.getTotalElements())
        .build();
  }

  // Page<Entity> → PageResponse<Dto>
  default <T, R> PageResponse<R> fromPage(Page<T> page, List<R> items) {
    return PageResponse.<R>builder()
        .content(items)
        .number(page.getNumber() + 1)
        .size(page.getSize())
        .hasNext(page.hasNext())
        .totalElements(page.getTotalElements())
        .build();
  }
}
