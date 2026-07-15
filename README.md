# Clínica Médica - CLI

Sistema de linha de comando (CLI) desenvolvido em Java para simulação de fluxo operacional de uma clínica médica. O projeto foca na aplicação prática de Programação Orientada a Objetos (POO), gerenciando fluxos de autenticação, herança de classes e controle de permissões.

## 🛠️ Tecnologias e Conceitos Aplicados
* **Java SE (LTS)**
* **Herança e Polimorfismo:** Especialização de atores a partir de uma classe base (`Pessoa` -> `Paciente`, `Médico`, `Secretária` -> `SecretáriaSupervisora`).
* **Encapsulamento:** Controle estrito de visibilidade e modificação de atributos de domínio (CRM, Matrícula, CPF).
* **Estruturas de Dados:** Manipulação de coleções em memória com `ArrayList` e controle de remoção via `Iterator`.

## 👥 Níveis de Acesso e Regras de Negócio
1. **Secretária Supervisora:** Instância única responsável por cadastrar/remover secretárias subordinadas, além de gerenciar seus próprios médicos e pacientes.
2. **Secretária:** Responsável por cadastrar e vincular pacientes e médicos. Cada secretária possui um limite rígido de vinculação de até **3 médicos**.
3. **Médicos e Pacientes:** Usuários finais com permissão de login baseada em credenciais cadastradas no sistema (CRM e CPF, respectivamente).
