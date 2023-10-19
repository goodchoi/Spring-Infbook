# 📖 Infbook
<img width=220 src = https://github.com/goodchoi/Spring-Infbook/assets/105799662/5e50f4c9-3ddb-47f5-96d7-9d17bd0c9cb0>


+ 프로젝트 URL : http://last-env-1.eba-pmmwigfd.ap-northeast-2.elasticbeanstalk.com/ (2023/10/03 ~ 현재 배포 중)



## 목차
[1. 프로젝트 개요](#1-프로젝트-개요)

[2. 프로젝트 상세 화면](#2-프로젝트-상세-화면)

[3. 프로젝트 주요 기능 설명](#3-프로젝트-주요-기능)

[4. 트러블 슈팅](#트러블-슈팅)

[5. 회고](#회고)
<br>

## 1. 프로젝트 개요
* #### 프로젝트 주제
    * 개발 서적을 집중적으로 다루는 오로지 개발자를 위한 도서 쇼핑몰 웹 어플리케이션 토이 프로젝트
* #### 프로젝트 목표
    * Spring Boot, JPA 등을 학습하며 정리한 내용을 바탕으로 DB 설계 - 구현 - 배포를 경험하는 것을 목표로 한다.
    * 기존에 학습한 내용외에 프로젝트에 필요한 기술이나 지식들을 추가적으로 학습하며 프로젝트를 진행한다.

* #### 사용 기술
    * ###### 백엔드
      ![Spring-Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white)
      ![SpringSecurity](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=SpringSecurity&logoColor=white)
      ![JWT](https://img.shields.io/badge/JWT-665544?style=flat-square&logo=jpa&logoColor=white)
      ![OAUTH2](https://img.shields.io/badge/OAUTH2-335581?style=flat-square&logo=jpa&logoColor=white)
      ![JPA](https://img.shields.io/badge/JPA-00555?style=flat-square&logo=jpa&logoColor=white)

    * ###### 프론트
      ![HTML](https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=HTML5&logoColor=white)
      ![CSS](https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=css3&logoColor=white)
      ![javascript](https://img.shields.io/badge/Javascript-F7DF1E?style=flat-square&logo=javascript&logoColor=white)
      ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=Thymeleaf&logoColor=white)
      ![JQUERY](https://img.shields.io/badge/Jquery-0769AD?style=flat-square&logo=jquery&logoColor=white)
      ![bootstrap](https://img.shields.io/badge/bootstrap-7952B3?style=flat-square&logo=bootstrap&logoColor=white)

    * ###### DB
      ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
      ![H2](https://img.shields.io/badge/H2-336699?style=flat-square&logo=H2&logoColor=white)

    * ###### CI/CD 및 배포
      ![github_actions](https://img.shields.io/badge/github_actions-2088FF?style=flat-square&logo=githubactions&logoColor=white)
      ![AWS EBS](https://img.shields.io/badge/aws_ElasticBeanstalk-d66414?style=flat-square&logo=AmazonAWS&logoColor=white)
      ![AWS EC2](https://img.shields.io/badge/Amazon_ec2-FF9900?style=flat-square&logo=amazonec2&logoColor=white)
      ![AWS RDS](https://img.shields.io/badge/Amazon_RDS-527FFF?style=flat-square&logo=amazonrds&logoColor=white)
      ![AWS S3](https://img.shields.io/badge/Amazon_S3-569A31?style=flat-square&logo=amazons3&logoColor=white)

    * ######  상세
        + Java: 17
        + IDE: IntelliJ IDEA
        + Spring Boot: 3.0.11
        + Build : Gradle

* #### DB MODEL
  <img width=800 src = https://github.com/goodchoi/Spring-Infbook/assets/105799662/46175d09-81f0-4d01-b046-1554deaf042a style="margin-left: 40px">

* #### ENTITY MODEL
  <img width=800 src= https://github.com/goodchoi/Spring-Infbook/assets/105799662/614b5053-2f82-4e9e-ac30-8dea42304234>

## 2. 프로젝트 상세 화면
|                                                                                                                                                                        |                                                                                                                                                                       |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------:| 
|           <img width=500 src = https://github.com/goodchoi/Spring-Infbook/assets/105799662/9e6e8b7f-8b92-44e4-ab20-130f917c03f4 ><br><center> 홈화면 </center>            | <img width=500 src = https://github.com/goodchoi/Spring-Infbook/assets/105799662/3a3c6555-2500-4287-bb31-73c7887402e8 ><br><center> OAuth2 카카오로그인 및 추가 회원가입 </center> |
|          <img width=500 src =https://github.com/goodchoi/Spring-Infbook/assets/105799662/19b6d003-1e64-484d-bcda-613e6b3b2637 ><br><center> 도서명 검색 </center>           |         <img width=500 src = https://github.com/goodchoi/Spring-Infbook/assets/105799662/63f4b9d7-edb4-47ba-9ca2-726ce5a82698 ><br><center> 상품상세 조회 </center>         |   
| <img width=500 src =https://github.com/goodchoi/Spring-Infbook/assets/105799662/0931fcab-6480-419f-9492-d2199f0a2784 ><br><center> 카테고리별 및 하위카테고리별 상품 리스트 조회 </center> |    <img width=500 src = https://github.com/goodchoi/Spring-Infbook/assets/105799662/4c63b170-6d86-4ba0-95fa-253579270d42 ><br><center> 장바구니에 상품 추가 및 삭제 </center>     |
|          <img width=500 src =https://github.com/goodchoi/Spring-Infbook/assets/105799662/ef19b01b-3004-4c30-938f-aaa2b73c665a ><br><center> 주문 및 결제 </center>          |          <img width=500 src = https://github.com/goodchoi/Spring-Infbook/assets/105799662/349d5304-a567-4bfd-bd81-dab55163b0b7 ><br><center> 에러페이지 </center>          |


---
## <u>작성 예정</u>
## 3. 프로젝트 주요 기능
   
## 4. 트러블 슈팅
## 5. 회고