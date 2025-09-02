-- Dados iniciais para teste
-- Usuários de autenticação para teste da aplicação
-- Username: admin / Password: 123456 (criptografada com SHA-256) - Role: ADMIN
-- Username: user / Password: 123456 (criptografada com SHA-256) - Role: USER

INSERT INTO users (username, password, role, ativo) 
VALUES 
('admin', 'a736BJcSxzKyvaWRBIVqAfo0xMJed4fGlGxSelJ1JR8=', 'ADMIN', true),
('user', 'a736BJcSxzKyvaWRBIVqAfo0xMJed4fGlGxSelJ1JR8=', 'USER', true);

-- Pessoas de exemplo para teste
INSERT INTO pessoas (nome, idade, cep, estado, cidade, bairro, logradouro, telefone, score, ativo) 
VALUES 
('João Silva', 35, '01310100', 'SP', 'São Paulo', 'Bela Vista', 'Avenida Paulista', '11999999999', 750, true),
('Maria Santos', 28, '20040020', 'RJ', 'Rio de Janeiro', 'Centro', 'Rua da Carioca', '21988888888', 850, true),
('Pedro Oliveira', 42, '30112000', 'MG', 'Belo Horizonte', 'Centro', 'Rua da Bahia', '31977777777', 650, true);
