package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "fileName", source = "fileName")
  @Mapping(target = "size", source = "size")
  @Mapping(target = "contentType", source = "contentType")
  BinaryContentDTO toDto(BinaryContent binaryContent);

  List<BinaryContentDTO> toDto(List<BinaryContent> binaryContents);
}
