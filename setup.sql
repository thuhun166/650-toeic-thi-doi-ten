-- =====================================================
-- Chay file nay trong bat ky database nao ma 1 server su dung.
-- Moi server dung database rieng, nhung cung chung cau truc bang.
-- =====================================================

CREATE TABLE IF NOT EXISTS print_jobs (
  job_id VARCHAR(100) PRIMARY KEY,
  document_content TEXT NOT NULL,
  requested_by VARCHAR(100) NOT NULL,
  requested_node INT NOT NULL,
  submitted_lamport BIGINT NOT NULL,
  submitted_at BIGINT NOT NULL,
  processed_node INT NULL,
  processed_lamport BIGINT NULL,
  processed_at BIGINT NULL,
  status VARCHAR(50) NOT NULL,
  note VARCHAR(255) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS ring_metadata (
  key_name VARCHAR(100) PRIMARY KEY,
  value_text VARCHAR(255) NOT NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
