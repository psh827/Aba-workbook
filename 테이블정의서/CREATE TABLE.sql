/*LEADERS*/
CREATE TABLE Leaders (
                         id varchar(10) NOT NULL PRIMARY KEY COMMENT '담당자 고유 ID (사용자 지정 문자열 or UUID)',
                         name VARCHAR(100) NOT NULL COMMENT '담당자 이름',
                         phone VARCHAR(20) NULL COMMENT '전화번호 (선택 입력)',
                         hire_date VARCHAR(8) NULL COMMENT '입사일',
                         auth_cd_no VARCHAR(3) NOT NULL COMMENT '권한코드',
                         child_id VARCHAR(10) NULL COMMENT '아이고유 ID',
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                         updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='담당자 정보 테이블';

/*CHILD*/
CREATE TABLE Child (
                       id VARCHAR(10) NOT NULL PRIMARY KEY COMMENT '아동 고유 ID (기본키)',
                       name VARCHAR(100) NOT NULL COMMENT '아동 이름',
                       age INT NULL DEFAULT 0 COMMENT '나이',
                       leader_id BIGINT NULL COMMENT '담당자 고유 ID',
                       mom_id VARCHAR(10) NULL COMMENT '엄마 고유 ID',
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                       updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='아동 정보 테이블';

/*Programs*/
CREATE TABLE Programs (
                          program_id VARCHAR(30) NOT NULL PRIMARY KEY COMMENT '프로그램 고유 ID (week_cnt + child_id + 증가번호)',
                          week_cnt VARCHAR(10) NOT NULL COMMENT '주차 정보 (예: 2501-1)',
                          child_id VARCHAR(10) NOT NULL COMMENT '아동 ID (users.id 참조)',
                          program_name VARCHAR(100) NOT NULL COMMENT '프로그램 이름 (예: 사물 기능 알기)',
                          session_cnt INT NULL DEFAULT 0 COMMENT '전체 세션 수 (null 가능, 기본 0)',
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                          updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                          CONSTRAINT fk_programs_child FOREIGN KEY (child_id) REFERENCES Child(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ABA 프로그램 테이블';

/*Program_items*/
CREATE TABLE Program_items (
                               item_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '항목 고유 ID',
                               program_id VARCHAR(30) NOT NULL COMMENT '상위 프로그램 ID (programs.program_id 참조)',
                               item_name VARCHAR(100) NOT NULL COMMENT '항목 이름 (예: 컵 가져오기, 문 열기 등)',
                               st VARCHAR(10) NULL DEFAULT '0' COMMENT '상태코드 (CommonCode 테이블 내 TDSC값 참조)',
                               tsm INT NULL DEFAULT 0 COMMENT '총 세션 중 행동 발생 수',
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                               updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                               CONSTRAINT fk_program_items_program FOREIGN KEY (program_id) REFERENCES Programs(program_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='프로그램 항목 테이블';

/*Program_notes*/
CREATE TABLE Program_notes (
                               note_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '노트 고유 ID',
                               program_id VARCHAR(30) NOT NULL COMMENT '상위 프로그램 ID (programs.program_id 참조)',
                               note TEXT NOT NULL COMMENT '자유 메모 텍스트 내용',
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                               updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                               CONSTRAINT fk_program_notes_program FOREIGN KEY (program_id) REFERENCES Programs(program_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='프로그램 노트 테이블';

/*Prompting_hierarchy*/
CREATE TABLE Prompting_hierarchy (
                                     hierarchy_id VARCHAR(30) NOT NULL PRIMARY KEY COMMENT '프로그램 고유 ID (week_cnt + child_id + 증가번호)',
                                     week_cnt VARCHAR(10) NOT NULL COMMENT '주차 정보 (예: 2501-1)',
                                     child_id VARCHAR(10) NOT NULL COMMENT '아동 ID (users.id 참조)',
                                     program_name VARCHAR(100) NOT NULL COMMENT '촉구 프로그램 이름 (예: Self-Help - Washing hands)',
                                     category VARCHAR(50) NULL COMMENT '영역 또는 분류 (예: 자기관리, 사회적 기술 등)',
                                     total_sessions INT NULL DEFAULT 0 COMMENT '총 세션 수 (전체 연습 횟수)',
                                     sitting_count INT NULL DEFAULT 0 COMMENT '시팅 수 (실제 촉구 평가 시도 횟수)',
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                                     updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                                     CONSTRAINT fk_hierarchy_child FOREIGN KEY (child_id) REFERENCES Child(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='촉구 프로그램 정보 테이블';

/*Prompting_items*/
CREATE TABLE Prompting_items (
                                 item_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '프로그램 항목 고유 ID',
                                 hierarchy_id VARCHAR(30) NOT NULL COMMENT '상위 촉구 프로그램 ID (prompting_hierarchy.hierarchy_id 참조)',
                                 child_id VARCHAR(10) NOT NULL COMMENT '아동 ID (users.id 참조)',
                                 item_name VARCHAR(100) NOT NULL COMMENT '아이템 명칭 (예: 실내)',
                                 item_suc_cnt INT NULL DEFAULT 0 COMMENT '아이템 성공 횟수',
                                 item_total_cnt INT NULL DEFAULT 0 COMMENT '아이템 총 시도 횟수',
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                                 updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                                 CONSTRAINT fk_prompting_items_hierarchy FOREIGN KEY (hierarchy_id) REFERENCES prompting_hierarchy(hierarchy_id),
                                 CONSTRAINT fk_prompting_items_child FOREIGN KEY (child_id) REFERENCES Child(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='촉구 아이템 기록 테이블';

/*Prompting_notes*/
CREATE TABLE Prompting_notes (
                                 note_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '메모 고유 ID',
                                 hierarchy_id VARCHAR(30) NOT NULL COMMENT '상위 촉구 프로그램 ID (prompting_hierarchy.hierarchy_id 참조)',
                                 item_id BIGINT NOT NULL COMMENT '프로그램 항목 ID (prompting_items.item_id 참조)',
                                 note_date DATE NOT NULL COMMENT '메모 발생일자 (예: 2025-04-23)',
                                 note TEXT NOT NULL COMMENT '자유 기록 메모 내용',
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                                 updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                                 CONSTRAINT fk_prompting_notes_hierarchy FOREIGN KEY (hierarchy_id) REFERENCES prompting_hierarchy(hierarchy_id),
                                 CONSTRAINT fk_prompting_notes_item FOREIGN KEY (item_id) REFERENCES prompting_items(item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='촉구 메모 기록 테이블';


/*Prompting_steps*/
CREATE TABLE Prompting_steps (
                                 step_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '촉구 단계 고유 ID',
                                 hierarchy_id VARCHAR(20) NOT NULL COMMENT '상위 촉구 프로그램 ID (prompting_hierarchy.hierarchy_id 참조)',
                                 seq INT NOT NULL COMMENT '단계 순서',
                                 step VARCHAR(50) NOT NULL COMMENT '단계 이름 또는 번호 (예: Step 1)',
                                 ind VARCHAR(10) NULL COMMENT '독립 수행 여부 (예: O, X)',
                                 hip VARCHAR(10) NULL COMMENT '신체적 도움 여부 (예: O, X)',
                                 note TEXT NULL COMMENT '해당 단계에 대한 코멘트 또는 기록',
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                                 updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                                 CONSTRAINT fk_prompting_steps_hierarchy FOREIGN KEY (hierarchy_id) REFERENCES prompting_hierarchy(hierarchy_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='촉구 단계 테이블';

/*Protests*/
CREATE TABLE Protests (
                          protest_id VARCHAR(30) NOT NULL PRIMARY KEY COMMENT '반항 고유 ID (week_cnt + child_id + 증가번호)',
                          child_id VARCHAR(10) NOT NULL COMMENT '아동 ID (users.id 참조)',
                          week_cnt VARCHAR(10) NOT NULL COMMENT '주차 정보 (예: 2501-1)',
                          protest_type ENUM('short', 'long') NOT NULL DEFAULT 'short' COMMENT '항의 유형 구분 (short 또는 long)',
                          protest_date VARCHAR(8) NULL COMMENT '발생 일시 (예: 20250503)',
                          protest_time VARCHAR(10) NULL COMMENT '발생 시간 (예: 16:35)',
                          protest_location VARCHAR(100) NULL COMMENT '장소 정보 (예: 화장실, 교실 등)',
                          staff_name VARCHAR(50) NULL COMMENT '담당자 이름',
                          staff_code VARCHAR(10) NULL COMMENT '담당자 코드 (leaders.id 참조)',
                          pre_condition TEXT NULL COMMENT '발생 전 상황 설명',
                          duration_sec INT NULL COMMENT '행동 지속 시간 (초 단위)',
                          function_type_cd VARCHAR(5) NULL COMMENT '기능 분석 유형 코드 (function_type.code 참조)',
                          result TEXT NULL COMMENT '대응 결과 요약',
                          behavior_code VARCHAR(5) NULL COMMENT '행동 코드 (behavior_code.code 참조)',
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                          updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                          CONSTRAINT fk_protests_child FOREIGN KEY (child_id) REFERENCES Child(id),
                          CONSTRAINT fk_protests_staff FOREIGN KEY (staff_code) REFERENCES Leaders(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='아동 항의 행동 기록 테이블';

/*Reinforcement_samplings*/
CREATE TABLE Reinforcement_samplings (
                                         sampling_id VARCHAR(30) NOT NULL PRIMARY KEY COMMENT '강화 샘플 고유 ID (week_cnt + child_id + 증가번호)',
                                         child_id VARCHAR(10) NOT NULL COMMENT '아동 ID (users.id 참조)',
                                         week_cnt VARCHAR(10) NOT NULL COMMENT '주차 정보 (예: 2501-1)',
                                         category VARCHAR(20) NOT NULL COMMENT '강화 종류 (예: 음식, 활동, 물건 등)',
                                         content VARCHAR(100) NOT NULL COMMENT '강화 항목 내용 (예: 젤리, 거품놀이 등)',
                                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                                         updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                                         CONSTRAINT fk_reinforcement_child FOREIGN KEY (child_id) REFERENCES Child(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='강화 샘플 테이블';

/*Reinforcement_notes*/
CREATE TABLE Reinforcement_notes (
                                     note_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '노트 고유 ID',
                                     sampling_id VARCHAR(30) NOT NULL COMMENT '상위 강화 샘플 ID (reinforcement_samplings.sampling_id 참조)',
                                     note TEXT NOT NULL COMMENT '강화 샘플 관련 자유 메모 내용',
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                                     updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                                     CONSTRAINT fk_reinforcement_notes_sampling FOREIGN KEY (sampling_id) REFERENCES reinforcement_samplings(sampling_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='강화 샘플 메모 테이블';

/*Session*/
CREATE TABLE Sessions (
                          week_cnt VARCHAR(10) NOT NULL COMMENT '세션 주 (2501-1, 2501-2)',
                          child_id VARCHAR(10) NOT NULL COMMENT '아동 ID (users.id 참조)',
                          home_sessions INT NULL DEFAULT 0 COMMENT '홈 환경 세션 횟수',
                          training_sessions INT NULL DEFAULT 0 COMMENT '센터 트레이닝 세션 횟수',
                          school_sessions INT NULL DEFAULT 0 COMMENT '학교 세션 횟수',
                          school_training_sessions INT NULL DEFAULT 0 COMMENT '학교 내 트레이닝 세션 횟수',
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                          updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                          PRIMARY KEY (week_cnt, child_id),
                          CONSTRAINT fk_sessions_child FOREIGN KEY (child_id) REFERENCES Child(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='주차별 세션 기록 테이블';

/*Tracking_data*/
CREATE TABLE Tracking_data (
                               id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '고유 ID',
                               child_id VARCHAR(10) NOT NULL COMMENT '아동 ID (users.id 참조)',
                               week_cnt VARCHAR(10) NOT NULL COMMENT '주차 정보 (예: 2501-1)',
                               behavior VARCHAR(100) NOT NULL COMMENT '행동 이름 (예: 뛰기, 손 들기)',
                               behavior_trials INT NULL DEFAULT 0 COMMENT '행동 시도 횟수',
                               trials_in_session FLOAT NULL COMMENT '세션 내 전체 시도 수',
                               data_type VARCHAR(10) NULL COMMENT '수치 단위 (예: %, /ssn 등)',
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                               updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                               CONSTRAINT fk_tracking_data_child FOREIGN KEY (child_id) REFERENCES Child(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='행동 추적 데이터 테이블';

/*Treatment_information*/
CREATE TABLE Treatment_information (
                                       treatment_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '치료 정보 고유 ID',
                                       child_id VARCHAR(10) NOT NULL COMMENT '아동 ID (users.id 참조)',
                                       week_cnt VARCHAR(10) NOT NULL COMMENT '주차 정보 (예: 2501-1)',
                                       staff_code VARCHAR(10) NOT NULL COMMENT '세션 담당자 ID (leaders.id 참조)',
                                       session_cnt INT NOT NULL DEFAULT 0 COMMENT '담당자가 수행한 세션 횟수',
                                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
                                       updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',

                                       CONSTRAINT fk_treatment_info_child FOREIGN KEY (child_id) REFERENCES Child(id),
                                       CONSTRAINT fk_treatment_info_staff FOREIGN KEY (staff_code) REFERENCES Leaders(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='치료 정보 기록 테이블';

/*Login_Info*/
CREATE TABLE Login_info (
    login_id VARCHAR(50) NOT NULL PRIMARY KEY COMMENT '로그인 아이디',
    login_pw VARCHAR(100) NOT NULL COMMENT '암호화된 로그인 비밀번호',
    login_type VARCHAR(4) NOT NULL COMMENT '로그인 사용자 유형 (선생, 엄마, 슈퍼바이저, 시스템관리자)',
    constraint_key VARCHAR(30) NOT NULL COMMENT '각 사용자 테이블의 PK (예: leaders.id, moms.id 등)',
    use_yn CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '사용 여부 (Y/N)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='통합 로그인 계정 테이블';

/*Refresh_Token*/
CREATE TABLE Refresh_tokens (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    refresh_token TEXT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_refresh_user FOREIGN KEY (user_id) REFERENCES users(id)
);

