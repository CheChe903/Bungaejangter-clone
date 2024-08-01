# 기능 명세서

## 개요

몰입캠프 4주 동안 하면서 느낀 것이 있다. 나는 급하게 4-1 캡스톤디자인을 수행하기 위해 Spring Boot 및 Spring JPA로 백엔드를 구현하는 것을 배웠다. (물론 그 전에, 인프런 김영한 선생님의 기본적인 강의는 들었다. 인간은 역시 망각의 동물 ..) 하지만, Spring Boot와 JPA를 몰입캠프에서 4주 동안 사용하다 보니, 왜 이게 이렇게 되는지, 이게 어떠한 어노테이션인지 헷갈리는 경우도 있었다.

기본적인 기능을 구현하는 데는 큰 문제가 없었지만, 더 깊이 있는 이해가 부족하다는 것을 느꼈다. 예를 들어, 특정 어노테이션의 동작 원리나 내부에서 실제로 발생하는 일에 대해 혼란스러울 때가 많았다. 또한, JPA의 경우, 영속성 컨텍스트와 엔티티 생명주기 등의 개념이 추상적으로만 이해되고, 실제 코드에서 어떻게 적용되는지 확신이 서지 않을 때가 있었다.

따라서 온전한 자바로만 해당 기능을 모두 구현해보고, SpringBoot 및 JPA로 리팩토링을 진행하면서 원리를 이해하고 과정에서 나오는 트러블 슈팅에 대해 자세하게 배울 것이다.

김영한 선생님의 강의를 한 번 다시 듣고 작성하는 것이 더 좋아보이지만 현재 근로장학을 하고있는 이슈로 미리 작성해보고, 내가 모르는 부분을 확실히 파악한 후 근로장학 이후 강의를 들으면 더 좋을 것 같다는 생각이 든다.

이러한 경험은 단순히 기능을 구현하는 것에서 더 나아가, 기술의 원리를 깊이 있게 이해하고 활용하는 것이 얼마나 중요한지를 깨닫게 할 수 있다. 앞으로는 단순히 코드를 작성하는 데 그치지 않고, 각 기술의 작동 원리와 이론적 배경을 충분히 이해하여 보다 안정적이고 확장 가능한 시스템을 구축할 수 있도록 노력해야겠다.

## 기능 목록

### 상품 관리
| 인덱스 | 메서드 | URI | 설명 | 명세 여부 |
|--------|--------|-----|------|-----------|
| 1      | GET    | /products | 모든 상품 조회 | O |
| 2      | GET    | /products?keyword={keyword} | 해당 키워드를 포함하는 제목의 상품 조회 | O |
| 3      | GET    | /products/{productId} | 특정 상품 조회 | O |
| 4      | GET    | /products/{productId}/images | 특정 상품 이미지 조회 | O |
| 5      | GET    | /products/{productId}/tags | 특정 상품 태그 조회 | O |
| 6      | POST   | /products/{productId}/favorites | 찜하기 | O |
| 7      | POST   | /products/new | 상품 등록 | O |
| 8      | POST   | /products/images | 상품 이미지 업로드 | O |
| 9      | PATCH  | /products/{productIdx}/edit | 특정 상품 수정 | O |
| 10     | PATCH  | /products/{productIdx}/status/ed | 특정 상품 판매상태 변경 | O |
| 11     | PATCH  | /products/{productIdx}/up | 특정 상품 UP하기 | O |
| 12     | DELETE | /products/{productIdx}/d | 특정 상품 삭제 | O |
| 13     | DELETE | /products/{productIdx}/favorites | 찜 해제 | O |

### 카테고리 관리
| 인덱스 | 메서드 | URI | 설명 | 명세 여부 |
|--------|--------|-----|------|-----------|
| 14     | GET    | /categories | 모든 카테고리 조회 | O |
| 15     | GET    | /categories/{categoryNum} | 카테고리별 상품 조회 | O |
| 16     | GET    | /categories/{categoryIdx}?order | 해당 카테고리에서 정렬기준에 따라 상품 조회 | O |
| 17     | POST   | /categories/new | 카테고리 등록 | O |
| 18     | PATCH  | /categories/{categoryIdx}/edit | 카테고리 수정 | O |
| 19     | DELETE | /categories/{categoryIdx}/d | 카테고리 삭제 | O |

### 사용자 관리
| 인덱스 | 메서드 | URI | 설명 | 명세 여부 |
|--------|--------|-----|------|-----------|
| 20     | GET    | /users | 모든 회원 조회 | O |
| 21     | GET    | /users/{userIdx} | 특정 회원 정보 조회 | O |
| 22     | GET    | /users/{userIdx}/products | 특정 회원의 판매상품 조회 | O |
| 23     | GET    | /users/{userIdx}/three-products | 특정 회원의 최신 3개 판매상품 조회 | O |
| 24     | GET    | /users/{userIdx}/products?status | 특정 회원의 판매상품 거래상태에 따라 조회 | O |
| 25     | GET    | /users/{userIdx}/products?order | 특정 회원의 판매상품 정렬기준에 따라 조회 | O |
| 26     | GET    | /users/{userIdx}/reviews | 특정 회원에 대한 추가 조회 | O |
| 27     | GET    | /users/{userIdx}/favorites | 특정 회원의 찜한 상품 조회 | O |
| 28     | GET    | /users/{userIdx}/followings | 특정 회원이 팔로우한 목록 조회 | O |
| 29     | GET    | /users/{userIdx}/followers | 특정 회원을 팔로우한 목록 조회 | O |
| 30     | POST   | /users/sign-up | 회원가입 | O |
| 31     | POST   | /users/log-in | 로그인 | O |

---

이 명세서는 각 기능에 대해 설명하며, 명세 여부란 해당 기능에 대해 구체적인 명세가 필요함을 나타냅니다.
