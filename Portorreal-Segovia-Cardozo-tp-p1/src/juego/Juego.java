package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Gondolf gondolf;
	private Murcielago[] murcielagos;
	private int totalMurcielagos = 200;
	private int murcielagosActivos = 0;
	private int maxMurcielagosEnPantalla = 6;
	boolean murcielagoEliminado = false;
	private Image fondo, botonera, menu, gameOver;
	private Botonera explosion1;
	private Botonera explosion2;
	private Botonera explosion3;
	private int tipoExplosionSeleccionada = 0; // 0 = ninguna, 1 = fuego, 2 = agua, 3 = veneno
	private int tipoExplosionActiva = 0;
	private double explosionX = -100;
	private double explosionY = -100;
	private int duracionExplosion = 0;
	private static final int duracionMaximaExplosion = 30; // 30 ticks = aprox. 0.5 segundos
	private Obstaculos[] rocas;
	private boolean juegoIniciado = false;
	private final int RADIO_FUEGO = 60;
	private final int RADIO_AGUA = 90;
	private final int RADIO_VENENO = 120;
	private Image victoria;
	private boolean juegoGanado = false;
	private Image botoneraSeleccionFuego;
	private Image botoneraSeleccionAgua;
	private Image botoneraSeleccionVeneno;
	private Image botoneraActual = botonera;
	private boolean mostrarImagenMana = false;
	private int contadorMostrarImagen = 0;
	private final int DURACION_IMAGEN_MANA = 90; // duración en frames (~3 segundos si 30 fps)
	private Image manaInsuficiente;
	private Image oleada1;
	private Image oleada2;
	private Image oleada3;
	private Image oleada4;
	private int oleada = 1;

	private void reiniciarJuego() {
		// Reiniciar gondolf
		gondolf = new Gondolf(700, 500); // posición inicial en el centro

		// Resetear contadores
		
		totalMurcielagos = 200;
		murcielagosActivos = 0;
		maxMurcielagosEnPantalla = 6;

		// Crear murciélagos
		murcielagos = new Murcielago[totalMurcielagos];
		int i = 0;
		for (int j = 0; j < 50; j++) {
			murcielagos[i++] = new Murcielago(-100, Math.random() * entorno.alto(), 30, 30, "izquierda");
			murcielagos[i++] = new Murcielago(1200, Math.random() * entorno.alto(), 30, 30, "derecha");
			murcielagos[i++] = new Murcielago(Math.random() * entorno.ancho() - 300, 0, 30, 30, "arriba");
			murcielagos[i++] = new Murcielago(Math.random() * entorno.ancho() - 300, entorno.alto() + 100, 30, 30,
					"abajo");
		}

		// Crear rocas con distancia mínima y margen
		rocas = new Obstaculos[15];
		int cantidadRocas = 0;
		int intentos = 0;
		int intentosMaximos = 1000;
		double margenBorde = 100;
		double distanciaMinima = 100;

		while (cantidadRocas < rocas.length && intentos < intentosMaximos) {
			double x = Math.random() * (1100 - 2 * margenBorde) + margenBorde;
			double y = Math.random() * (1000 - 2 * margenBorde) + margenBorde;

			boolean valido = true;

			// Evitar cercanía con otras rocas
			for (int r = 0; r < cantidadRocas; r++) {
				double dx = x - rocas[r].getX();
				double dy = y - rocas[r].getY();
				if (Math.sqrt(dx * dx + dy * dy) < distanciaMinima) {
					valido = false;
					break;
				}
			}

			// Evitar zona inicial del mago
			double dxM = x - 700;
			double dyM = y - 500;
			if (Math.sqrt(dxM * dxM + dyM * dyM) < 100) {
				valido = false;
			}

			if (valido) {
				rocas[cantidadRocas++] = new Obstaculos(x, y);
			}
			intentos++;
		}

		// Reiniciar estado de hechizos
		tipoExplosionSeleccionada = 0;
		explosionX = -100;
		explosionY = -100;
		duracionExplosion = 0;
		oleada = 1;

		// Estado general
		juegoIniciado = true;
		juegoGanado = false;
	}

	// Variables y métodos propios de cada grupo
	// ...

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Portorreal-Segovia-Cardozo-tp-p1", 1400, 1000);
		this.gondolf = new Gondolf(550, 500); // centro de la pantalla
		this.fondo = Herramientas.cargarImagen("Imagenes TP Programacion 1/Fondo.jpg").getScaledInstance(1100, 1000,
				Image.SCALE_SMOOTH);
		this.botonera = Herramientas.cargarImagen("Imagenes TP Programacion 1/Botonera.jpg").getScaledInstance(300,
				1000, Image.SCALE_SMOOTH);
		// menu//
		this.menu = Herramientas.cargarImagen("Imagenes TP Programacion 1/menu.jpg").getScaledInstance(1400, 1000,
				Image.SCALE_SMOOTH);
		;
		this.gameOver = Herramientas.cargarImagen("Imagenes TP Programacion 1/gameOver.jpg").getScaledInstance(1400,
				1000, Image.SCALE_SMOOTH);
		this.victoria = Herramientas.cargarImagen("Imagenes TP Programacion 1/Victoria.jpg").getScaledInstance(1400,
				1000, Image.SCALE_SMOOTH);

		// imagenes botonera
		this.botoneraSeleccionFuego = Herramientas
				.cargarImagen("Imagenes TP Programacion 1/BotoneraFuegoSeleccionado.gif")
				.getScaledInstance(300, 1000, 0);
		this.botoneraSeleccionAgua = Herramientas
				.cargarImagen("Imagenes TP Programacion 1/BotoneraHieloSeleccionado.gif")
				.getScaledInstance(300, 1000, 0);
		this.botoneraSeleccionVeneno = Herramientas
				.cargarImagen("Imagenes TP Programacion 1/BotoneraVenenoSeleccionado.gif")
				.getScaledInstance(300, 1000, 0);
		
		this.oleada1 = Herramientas.cargarImagen("Imagenes TP Programacion 1/oleada1.png").getScaledInstance(250, 500, Image.SCALE_SMOOTH);
		this.oleada2= Herramientas.cargarImagen("Imagenes TP Programacion 1/oleada2.png").getScaledInstance(250, 500, Image.SCALE_SMOOTH);
		this.oleada3= Herramientas.cargarImagen("Imagenes TP Programacion 1/oleada3.png").getScaledInstance(250, 500, Image.SCALE_SMOOTH);
		this.oleada4= Herramientas.cargarImagen("Imagenes TP Programacion 1/oleada4.png").getScaledInstance(250, 500, Image.SCALE_SMOOTH);

		this.manaInsuficiente = Herramientas.cargarImagen("Imagenes TP Programacion 1/ManaInsuficiente.png")
				.getScaledInstance(150, 250, Image.SCALE_SMOOTH);

		// murcielagos//
		murcielagos = new Murcielago[totalMurcielagos];
		int i = 0;
		for (int j = 0; j < 50; j++) {
			murcielagos[i++] = new Murcielago(-100, Math.random() * entorno.alto(), 30, 30, "izquierda");
			murcielagos[i++] = new Murcielago(1200, Math.random() * entorno.alto(), 30, 30, "derecha");
			murcielagos[i++] = new Murcielago(Math.random() * entorno.ancho() - 300, 0, 30, 30, "arriba");
			murcielagos[i++] = new Murcielago(Math.random() * entorno.ancho() - 300, entorno.alto() + 100, 30, 30,
					"abajo");
		}

		// Inicializar explosiones (ANTES del for)
		this.explosion1 = new Botonera(0, 0);
		this.explosion2 = new Botonera(0, 0);
		this.explosion3 = new Botonera(0, 0);

		// rocas
		rocas = new Obstaculos[15];
		int cantidadRocas = 0;
		int intentosMaximos = 1000;
		int intentos = 0;
		double distanciaMinima = 100;
		double margenBorde = 100;

		while (cantidadRocas < rocas.length && intentos < intentosMaximos) {
			// Genera coordenadas aleatorias dentro del área jugable con margen
			double x = Math.random() * (1100 - 2 * margenBorde) + margenBorde;
			double y = Math.random() * (1000 - 2 * margenBorde) + margenBorde;

			boolean posicionValida = true;

			for (int j = 0; j < cantidadRocas; j++) {
				if (rocas[j] != null) {
					double dx = x - rocas[j].getX();
					double dy = y - rocas[j].getY();
					double distancia = Math.sqrt(dx * dx + dy * dy);

					if (distancia < distanciaMinima) {
						posicionValida = false;
						break;
					}
					double dxMago = x - 550;
					double dyMago = y - 500;
					double distanciaAMago = Math.sqrt(dxMago * dxMago + dyMago * dyMago);
					if (distanciaAMago < 100) {
						posicionValida = false;
					}
				}
			}

			if (posicionValida) {
				rocas[cantidadRocas] = new Obstaculos(x, y);
				cantidadRocas++;
			}

			intentos++;
		}

		this.entorno.iniciar();
	}

	public void tick() {
		if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
			System.out.println("x=" + entorno.mouseX() + "y= " + entorno.mouseY());
		}

		if (!juegoIniciado) {
			entorno.dibujarImagen(menu, entorno.ancho() / 2, entorno.alto() / 2, 0, 1);
			if (entorno.mouseX() >= 870 && entorno.mouseX() <= 1180 && entorno.mouseY() >= 650
					&& entorno.mouseY() <= 780)// si el mouse esta ubicado en el boton
				if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {// si el click del mouse fue apretado
					juegoIniciado = true; // Inicia el juego
				}

			return;
		}
		if (gondolf == null) {
			entorno.dibujarImagen(gameOver, entorno.ancho() / 2, entorno.alto() / 2, 0, 1);
			if (entorno.mouseX() >= 444 && entorno.mouseX() <= 784 && entorno.mouseY() >= 745
					&& entorno.mouseY() <= 814)
				if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {

					// REINICIALIZAR TODO
					reiniciarJuego();
					return;
				}
			return;
		}

		if (totalMurcielagos <= 0 && murcielagosActivos <= 0) {// detecta si ganaste el juego y lo hace true
			juegoGanado = true;
		}

		if (juegoGanado) {
			entorno.dibujarImagen(victoria, entorno.ancho() / 2, entorno.alto() / 2, 0, 1);

			if (entorno.mouseX() >= 520 && entorno.mouseX() <= 880 && entorno.mouseY() >= 500 && entorno.mouseY() <= 770
					&& entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {

				// REINICIALIZAR TODO
				reiniciarJuego();
				return;
			}
			return;
		}

//////////                     INTERFAZ                  /////////////////////////////////////////////////////////
		entorno.dibujarImagen(fondo, 550, 500, 0);
		// Dibujar las Rocas
		for (int i = 0; i < rocas.length; i++) {
			if (rocas[i] != null) {
				rocas[i].dibujarRoca(entorno);
			}
		}

///////////////////Gondolf////////////////////////////////////////////////////////////
		if (entorno.estaPresionada('w') || entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
			gondolf.moverArriba(rocas);
		}
		if (entorno.estaPresionada('s') || entorno.estaPresionada(entorno.TECLA_ABAJO)) {
			gondolf.moverAbajo(rocas);
		}
		if (entorno.estaPresionada('a') || entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
			gondolf.moverIzquierda(rocas);
		}
		if (entorno.estaPresionada('d') || entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			gondolf.moverDerecha(rocas);
		}
		gondolf.dibujar(entorno);
		if (gondolf.colisionaConRoca(rocas, gondolf.getX(), gondolf.getY())) {
			// System.out.println("¡Gondolf colisionó con una roca!");
		}
		if (gondolf.colisionaEnemigo(murcielagos)) {
			murcielagosActivos--;
			totalMurcielagos--;
			// System.out.println("¡Gondolf fue golpeado por un murciélago!");
		}
		if (gondolf.getVida() == 0) {
			gondolf = null;
		}

		// System.out.println("Vida de Gondolf: " + gondolf.getVida());

//////////////////////////////Botonera///////////////////////////////		
		// explosion

		if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
			int mouseX = entorno.mouseX();
			int mouseY = entorno.mouseY();

			if (mouseX >= 1150 && mouseX <= 1340 && mouseY >= 340 && mouseY <= 420) {
				tipoExplosionSeleccionada = 1; // Fuego
			} else if (mouseX >= 1150 && mouseX <= 1340 && mouseY >= 510 && mouseY <= 600) {
				tipoExplosionSeleccionada = 2; // Agua
			} else if (mouseX >= 1150 && mouseX <= 1340 && mouseY >= 680 && mouseY <= 770) {
				tipoExplosionSeleccionada = 3; // Veneno
			} else if (tipoExplosionSeleccionada != 0) {
				boolean puedeLanzar = true;

				if (tipoExplosionSeleccionada == 2 && gondolf.getMana() < 10) {
					puedeLanzar = false;
					mostrarImagenMana = true;
					contadorMostrarImagen = 0;
				} else if (tipoExplosionSeleccionada == 3 && gondolf.getMana() < 20) {
					puedeLanzar = false;
					mostrarImagenMana = true;
					contadorMostrarImagen = 0;
				}

				if (puedeLanzar) {
					explosionX = mouseX;
					explosionY = mouseY;
					duracionExplosion = duracionMaximaExplosion; // <- Aquí pones 120 o más
					tipoExplosionActiva = tipoExplosionSeleccionada;
					tipoExplosionSeleccionada = 0;

					if (tipoExplosionActiva == 2)
						gondolf.restarMana(10);
					else if (tipoExplosionActiva == 3)
						gondolf.restarMana(50);
				}
			}
		}
		if (mostrarImagenMana) {
			contadorMostrarImagen++;
			if (contadorMostrarImagen > DURACION_IMAGEN_MANA) {
				mostrarImagenMana = false;
			}
		}

		// En el método de dibujo y actualización del juego:
		if (duracionExplosion > 0) {
			if (tipoExplosionActiva == 1) {
				explosion1.setX(explosionX);
				explosion1.setY(explosionY);
				explosion1.dibujarExplosion1(entorno);
			} else if (tipoExplosionActiva == 2) {
				explosion2.setX(explosionX);
				explosion2.setY(explosionY);
				explosion2.dibujarExplosion2(entorno);
			} else if (tipoExplosionActiva == 3) {
				explosion3.setX(explosionX);
				explosion3.setY(explosionY);
				explosion3.dibujarExplosion3(entorno);
			}
			duracionExplosion--;
		} else {
			tipoExplosionActiva = 0; // Explosión terminó
		}

		if (duracionExplosion > 0) {
			for (int i = 0; i < murcielagos.length; i++) {
				if (murcielagos[i] != null && murcielagos[i].estaActivo()) {
					double dx = murcielagos[i].getX() - explosionX;
					double dy = murcielagos[i].getY() - explosionY;
					double distancia = Math.sqrt(dx * dx + dy * dy);
					int radioActual = 0;

					if (tipoExplosionActiva == 1)
						radioActual = RADIO_FUEGO;
					else if (tipoExplosionActiva == 2)
						radioActual = RADIO_AGUA;
					else if (tipoExplosionActiva == 3)
						radioActual = RADIO_VENENO;

					if (distancia < radioActual) {
						murcielagos[i] = null;
						murcielagosActivos--;
						totalMurcielagos--;
						gondolf.sumarMana(5);
					}
				}
			}
		}

// DIBUJAR EXPLOSION (si está activa y en tiempo)
		if (tipoExplosionSeleccionada != 0 && duracionExplosion > 0) {
			if (tipoExplosionSeleccionada == 1) {
				explosion1.setX(explosionX);
				explosion1.setY(explosionY);
				explosion1.dibujarExplosion1(entorno);
			} else if (tipoExplosionSeleccionada == 2) {
				explosion2.setX(explosionX);
				explosion2.setY(explosionY);
				explosion2.dibujarExplosion2(entorno);
			} else if (tipoExplosionSeleccionada == 3) {
				explosion3.setX(explosionX);
				explosion3.setY(explosionY);
				explosion3.dibujarExplosion3(entorno);
			}

			duracionExplosion--;
		}

// Eliminar murciélagos cerca de la explosión
// Daño por explosión si está activa
		if (duracionExplosion > 0 && tipoExplosionSeleccionada != 0) {
			for (int i = 0; i < murcielagos.length; i++) {
				if (murcielagos[i] != null && murcielagos[i].estaActivo()) {
					double dx = murcielagos[i].getX() - explosionX;
					double dy = murcielagos[i].getY() - explosionY;
					double distancia = Math.sqrt(dx * dx + dy * dy);
					int radioActual = 0;
					if (tipoExplosionSeleccionada == 1)
						radioActual = RADIO_FUEGO;
					else if (tipoExplosionSeleccionada == 2)
						radioActual = RADIO_AGUA;
					else if (tipoExplosionSeleccionada == 3)
						radioActual = RADIO_VENENO;

					if (distancia < radioActual) {
						murcielagos[i] = null;
						murcielagosActivos--;
						totalMurcielagos--;
						gondolf.sumarMana(5);
					}
				}
			}

		}

///////////////murcielagos////////////////////////////////////////////////////
		// Activar hasta 20
        for (int i = 0; i < murcielagos.length && murcielagosActivos < maxMurcielagosEnPantalla; i++) {
            if (murcielagos[i] != null && !murcielagos[i].estaActivo()) {
                murcielagos[i].activar();
                murcielagosActivos++;
            }
            if (totalMurcielagos < 150) {
            	oleada=2;
                maxMurcielagosEnPantalla = 9 ;
            }
            if (totalMurcielagos < 100) {
            	oleada=3;
                maxMurcielagosEnPantalla = 12;
            }
            if (totalMurcielagos < 50) {
            	oleada=4;
                maxMurcielagosEnPantalla = 15;
            }
        }

		for (int i = 0; i < murcielagos.length; i++) {
			if (murcielagos[i] != null && murcielagos[i].estaActivo()) {
				murcielagos[i].moverHacia(gondolf.getX(), gondolf.getY());
				murcielagos[i].dibujar(entorno);
			}
		}
		// dibuja la botonera y la informacion despues de los murcielagos
		if (tipoExplosionSeleccionada == 1)
			botoneraActual = botoneraSeleccionFuego;
		else if (tipoExplosionSeleccionada == 2)
			botoneraActual = botoneraSeleccionAgua;
		else if (tipoExplosionSeleccionada == 3)
			botoneraActual = botoneraSeleccionVeneno;
		else
			botoneraActual = botonera;

		entorno.dibujarImagen(botoneraActual, 1250, 500, 0);

		entorno.cambiarFont("ARCADE", 12, Color.WHITE);
		entorno.escribirTexto("" + gondolf.getVida(), 1300, 470);
		entorno.cambiarFont("Arial", 12, Color.WHITE);
		entorno.escribirTexto("" + totalMurcielagos, 1250, 320);

		entorno.cambiarFont("ARCADE", 12, Color.WHITE);
		entorno.escribirTexto("" + gondolf.getMana(), 1300, 640);
		
		if(oleada==1) {
			entorno.dibujarImagen(oleada1, 1250, 105, 0);;
		}
		if(oleada==2) {
			entorno.dibujarImagen(oleada2, 1250, 105, 0);;
		}
		if(oleada==3) {
			entorno.dibujarImagen(oleada3, 1250, 105, 0);;
		}
		if(oleada==4) {
			entorno.dibujarImagen(oleada4, 1250, 105, 0);;
		}
		
		if (mostrarImagenMana) {
			entorno.dibujarImagen(manaInsuficiente, 1250, 950, 0);
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
