import java.util.*;

class Pessoa {
    protected String nome, email, telefone, whatsapp, cpf;

    public Pessoa(String nome, String email, String telefone, String whatsapp, String cpf) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.whatsapp = whatsapp;
        this.cpf = cpf;
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
}

class Paciente extends Pessoa {
    public Paciente(String nome, String email, String telefone, String whatsapp, String cpf) {
        super(nome, email, telefone, whatsapp, cpf);
    }
}

class Medico extends Pessoa {
    private String crm;

    public Medico(String nome, String email, String telefone, String whatsapp, String cpf, String crm) {
        super(nome, email, telefone, whatsapp, cpf);
        this.crm = crm;
    }

    public String getCrm() { return crm; }
}

class Secretaria extends Pessoa {
    protected String matricula, cargo;
    protected List<Medico> medicos = new ArrayList<>();
    protected List<Paciente> pacientes = new ArrayList<>();

    public Secretaria(String nome, String email, String telefone, String whatsapp, String cpf, String matricula, String cargo) {
        super(nome, email, telefone, whatsapp, cpf);
        this.matricula = matricula;
        this.cargo = cargo;
    }

    public String getMatricula() { return matricula; }
    public String getCargo() { return cargo; }
    public boolean vincularMedico(Medico m) {
        if (medicos.size() >= 3) return false;
        medicos.add(m);
        return true;
    }
    public void cadastrarPaciente(Paciente p) { pacientes.add(p); }
    public List<Medico> getMedicos() { return medicos; }
    public List<Paciente> getPacientes() { return pacientes; }
}

class SecretariaSupervisora extends Secretaria {
    private List<Secretaria> secretarias = new ArrayList<>();

    public SecretariaSupervisora(String nome, String email, String telefone, String whatsapp, String cpf, String matricula, String cargo) {
        super(nome, email, telefone, whatsapp, cpf, matricula, cargo);
    }

    public void cadastrarSecretaria(Secretaria s) { secretarias.add(s); }
    public List<Secretaria> getSecretarias() { return secretarias; }
    public boolean excluirSecretaria(String matricula) {
        return secretarias.removeIf(s -> s.getMatricula().equals(matricula));
    }
    public Secretaria buscarSecretaria(String matricula) {
        for (Secretaria s : secretarias) if (s.getMatricula().equals(matricula)) return s;
        return null;
    }
    public Medico removerMedico(String crm) {
        Iterator<Medico> itSup = this.getMedicos().iterator();
        while (itSup.hasNext()) {
            Medico m = itSup.next();
            if (m.getCrm().equals(crm)) {
                itSup.remove();
                return m;
            }
        }

        for (Secretaria s : secretarias) {
            Iterator<Medico> it = s.getMedicos().iterator();
            while (it.hasNext()) {
                Medico m = it.next();
                if (m.getCrm().equals(crm)) {
                    it.remove();
                    return m;
                }
            }
        }
        return null;
    }
}

class ClinicaMedica {
    static Scanner scanner = new Scanner(System.in);
    static SecretariaSupervisora supervisora = new SecretariaSupervisora("Supervisora", "sup@email.com", "0000", "0000", "000.000.000-00", "0001", "Supervisora");
    static int tentativas = 3;

    public static void main(String[] args) {
        while (tentativas > 0) {
            System.out.println("\nEntrar como:\n1 - Paciente\n2 - Médico\n3 - Secretária\n0 - Sair");
            System.out.print("Escolha: ");
            try {
                int op = Integer.parseInt(scanner.nextLine());
                switch (op) {
                    case 1 -> autenticarPaciente();
                    case 2 -> autenticarMedico();
                    case 3 -> autenticarSecretaria();
                    case 0 -> System.exit(0);
                    default -> System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
        System.out.println("Limite de tentativas excedido. Encerrando...");
    }

    private static void autenticarPaciente() {
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        for (Paciente p : supervisora.getPacientes()) {
            if (p.getCpf().equals(cpf)) {
                System.out.println("Bem-vindo(a), Paciente " + p.getNome());
                return;
            }
        }

        for (Secretaria s : supervisora.getSecretarias()) {
            for (Paciente p : s.getPacientes()) {
                if (p.getCpf().equals(cpf)) {
                    System.out.println("Bem-vindo(a), Paciente " + p.getNome());
                    return;
                }
            }
        }
        erroAutenticacao();
    }

    private static void autenticarMedico() {
        System.out.print("CRM: ");
        String crm = scanner.nextLine();

        for (Medico m : supervisora.getMedicos()) {
            if (m.getCrm().equals(crm)) {
                System.out.println("Bem-vindo(a), Dr(a). " + m.getNome());
                return;
            }
        }

        for (Secretaria s : supervisora.getSecretarias()) {
            for (Medico m : s.getMedicos()) {
                if (m.getCrm().equals(crm)) {
                    System.out.println("Bem-vindo(a), Dr(a). " + m.getNome());
                    return;
                }
            }
        }
        erroAutenticacao();
    }

    private static void autenticarSecretaria() {
        System.out.print("Matrícula: ");
        String mat = scanner.nextLine();
        if (supervisora.getMatricula().equals(mat)) {
            System.out.println("Bem-vindo(a), Secretária Supervisora " + supervisora.getNome());
            menuSupervisora();
            return;
        }
        for (Secretaria s : supervisora.getSecretarias()) {
            if (s.getMatricula().equals(mat)) {
                System.out.println("Bem-vindo(a), Secretária " + s.getNome());
                menuSecretaria(s);
                return;
            }
        }
        erroAutenticacao();
    }

    private static void erroAutenticacao() {
        tentativas--;
        System.out.println("Credenciais inválidas. Tentativas restantes: " + tentativas);
    }

    private static void menuSupervisora() {
        while (true) {
            System.out.println("\nMenu Supervisora:\n1 - Cadastrar secretária\n2 - Listar secretárias\n3 - Excluir secretária\n4 - Cadastrar médico\n5 - Listar médicos\n6 - Remover médico\n7 - Cadastrar paciente\n8 - Listar pacientes\n0 - Voltar");
            System.out.print("Escolha: ");
            try {
                int op = Integer.parseInt(scanner.nextLine());
                switch (op) {
                    case 1 -> cadastrarSecretaria();
                    case 2 -> supervisora.getSecretarias().forEach(s -> System.out.println(s.getNome()));
                    case 3 -> {
                        System.out.print("Matrícula: ");
                        String mat = scanner.nextLine();
                        System.out.println(supervisora.excluirSecretaria(mat) ? "Excluída." : "Não encontrada.");
                    }
                    case 4 -> menuCadastrarMedico(supervisora);
                    case 5 -> {
                        supervisora.getMedicos().forEach(m -> System.out.println(m.getNome() + " (Gerido por: Supervisora)"));
                        supervisora.getSecretarias().forEach(s -> s.getMedicos().forEach(m -> System.out.println(m.getNome() + " (Gerido por: " + s.getNome() + ")")));
                    }
                    case 6 -> {
                        System.out.print("CRM do médico: ");
                        Medico removido = supervisora.removerMedico(scanner.nextLine());
                        System.out.println(removido != null ? "Removido: " + removido.getNome() : "Não encontrado.");
                    }
                    case 7 -> menuCadastrarPaciente(supervisora);
                    case 8 -> {
                        supervisora.getPacientes().forEach(p -> System.out.println(p.getNome() + " (Gerido por: Supervisora)"));
                        supervisora.getSecretarias().forEach(s -> s.getPacientes().forEach(p -> System.out.println(p.getNome() + " (Gerido por: " + s.getNome() + ")")));
                    }
                    case 0 -> { return; }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }
    }

    private static void menuSecretaria(Secretaria s) {
        while (true) {
            System.out.println("\nMenu Secretária:\n1 - Cadastrar médico\n2 - Cadastrar paciente\n3 - Listar médicos\n4 - Listar pacientes\n0 - Voltar");
            System.out.print("Escolha: ");
            try {
                int op = Integer.parseInt(scanner.nextLine());
                switch (op) {
                    case 1 -> menuCadastrarMedico(s);
                    case 2 -> menuCadastrarPaciente(s);
                    case 3 -> s.getMedicos().forEach(m -> System.out.println(m.getNome()));
                    case 4 -> s.getPacientes().forEach(p -> System.out.println(p.getNome()));
                    case 0 -> { return; }
                    default -> System.out.println("Inválido");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }
    }

    private static void cadastrarSecretaria() {
        System.out.print("Nome: "); String nome = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Telefone: "); String tel = scanner.nextLine();
        System.out.print("WhatsApp: "); String zap = scanner.nextLine();
        System.out.print("CPF: "); String cpf = scanner.nextLine();
        System.out.print("Matrícula: "); String mat = scanner.nextLine();
        Secretaria s = new Secretaria(nome, email, tel, zap, cpf, mat, "Secretária");
        supervisora.cadastrarSecretaria(s);
        System.out.println("Cadastrada com sucesso.");
    }

    private static void menuCadastrarMedico(Secretaria s) {
        if (s.getMedicos().size() >= 3) {
            System.out.println("Limite de 3 médicos atingido.");
            return;
        }
        System.out.print("Nome: "); String nome = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Telefone: "); String tel = scanner.nextLine();
        System.out.print("WhatsApp: "); String zap = scanner.nextLine();
        System.out.print("CPF: "); String cpf = scanner.nextLine();
        System.out.print("CRM: "); String crm = scanner.nextLine();
        Medico m = new Medico(nome, email, tel, zap, cpf, crm);
        s.vincularMedico(m);
        System.out.println("Médico cadastrado.");
    }

    private static void menuCadastrarPaciente(Secretaria s) {
        System.out.print("Nome: "); String nome = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Telefone: "); String tel = scanner.nextLine();
        System.out.print("WhatsApp: "); String zap = scanner.nextLine();
        System.out.print("CPF: "); String cpf = scanner.nextLine();
        Paciente p = new Paciente(nome, email, tel, zap, cpf);
        s.cadastrarPaciente(p);
        System.out.println("Paciente cadastrado.");
    }
}
