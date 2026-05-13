public enum TipoLeito {
	ENFERMARIA {
		@Override
		public double calcularValor(int dias) {
			if (dias <=3) return 40.0 * dias;
			if (dias <= 8) return 35.0 * dias;
			return 30.0 * dias;
		}
	}, APARTAMENTO {
		@Override
		public double calcularValor(int dias) {
			if (dias <=3) return 100.0 * dias;
			if (dias <= 8) return 90.0 * dias;
			return 80.0 * dias;
		}
	};
		public abstract double calcularValor(int dias);
}

// Eu passei toda a lógica de preços que estava em Prontuário pra cá. O TipoLeito é quem sabe quanto custa cada diária, logo, ele é o "especialista" que deve conter a lógica de cálculo. Isso remove os if/else gigantes do Prontuário.

// Agora, se o hospital criar um novo tipo de leito, eu apenas adiciono no Enum. Aew eu fecho a classe Prontuario para modificações e posso abrir para extensões através dos Enums.
