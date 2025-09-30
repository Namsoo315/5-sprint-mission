package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  //바이너리 데이터의 저장/로드를 담당하는 컴포넌트입니다.
  UUID save(UUID id, byte[] data);

  //키 정보를 바탕으로 byte[] 데이터를 읽어 InputStream 타입으로 반환합니다.
  InputStream get(UUID id);

  //BinaryContentDto 정보를 바탕으로 파일을 다운로드할 수 있는 응답을 반환합니다.
  ResponseEntity<Resource> download(BinaryContentDTO binaryContentDTO);

}
