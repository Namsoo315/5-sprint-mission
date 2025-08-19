# 프로젝트 마일스톤 & 요구사항

## 🚀 프로젝트 마일스톤

- RESTful API로 재설계 및 리팩토링
- Swagger를 활용한 API 문서 자동화
- 프론트엔드 연동
- PaaS를 활용한 배포

---

## 📋 기본 요구사항

1. **RESTful API 재설계**
    - 스프린트 미션#4에서 구현한 API를 RESTful API로 다시 설계
    - API 스펙을 확인하고 본인이 설계한 API와 비교
    - `oasdiff`를 활용하면 API 비교가 수월함
    - 제공된 API 스펙에 맞추어 구현 (심화 요구사항 프론트엔드 연동을 위해 필수)

2. **API 테스트**
    - Postman을 활용하여 컨트롤러 테스트
    - 테스트 결과를 export하여 PR에 첨부

3. **Swagger 기반 API 문서화**
    - `springdoc-openapi` 활용
    - Swagger-UI를 통해 API 테스트 가능

---

## ✨ 심화 요구사항

1. **정적 리소스 서빙**
    - 제공된 `fe_1.0.0.zip` 활용
    - API 스펙을 준수하면 프론트엔드와 정상 연동

2. **PaaS 배포 (Railway.app)**
    - Railway.app 가입 및 GitHub 레포지토리 연결
    - `Settings > Network` 섹션에서 Generate Domain 버튼으로 도메인 생성
    - 생성된 도메인 접속 후 애플리케이션 테스트
