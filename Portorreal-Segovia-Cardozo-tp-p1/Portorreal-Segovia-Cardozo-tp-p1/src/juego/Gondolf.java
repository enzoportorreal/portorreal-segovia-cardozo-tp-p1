package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Gondolf {
	private double x;
	private double y;
	private int ancho; // ancho imagen mago
	private int alto; // alto imagen mago
	private double velocidad;
	private Image imagenAbajo;
	private Image imagenArriba;
	private Image imagenDerecha;
	private Image imagenIzquierda;
	private Image imagenActual;
	public static final int ANCHO_PANTALLA = 1400; // estas tres variantes las ajustamos segun la resolucion
	public static final int ALTO_PANTALLA = 1000;
	public static final int ANCHO_MENU = 300; // esto es el ancho de la botonera
	private int vida = 100;
	private int mana = 100;

	public Gondolf(double x, double y) {
		this.x = x;
		this.y = y;
		this.velocidad = 3;
		this.ancho = 40;
		this.alto = 40;
		this.imagenDerecha = Herramientas.cargarImagen("Imagenes TP Programacion 1/Mago Derecha Opcion 2.png")
				.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		this.imagenIzquierda = Herramientas.cargarImagen("Imagenes TP Programacion 1/Mago Izquierda Opcion 2.png")
				.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		this.imagenAbajo = Herramientas.cargarImagen("Imagenes TP Programacion 1/Mago Abajo Opcion 2.png")
				.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		this.imagenArriba = Herramientas.cargarImagen("Imagenes TP Programacion 1/Mago Arriba Opcion 2.png")
				.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		// empezamos con la imagen a la derecha
		this.imagenActual = imagenDerecha;

	}

	public void moverArriba(Obstaculos[] rocas) {
		double nuevaY = this.y - velocidad;
		if (nuevaY > alto / 2 && !colisionaConRoca(rocas, this.x, nuevaY)) {
			this.y = nuevaY;
			this.imagenActual = imagenArriba;
		}
	}

	public void moverAbajo(Obstaculos[] rocas) {
		double nuevaY = this.y + velocidad;
		if (nuevaY < ALTO_PANTALLA - alto / 2 && !colisionaConRoca(rocas, this.x, nuevaY)) {
			this.y = nuevaY;
			this.imagenActual = imagenAbajo;
		}
	}

	public void moverIzquierda(Obstaculos[] rocas) {
		double nuevaX = this.x - velocidad;
		if (nuevaX > ancho / 2 && !colisionaConRoca(rocas, nuevaX, this.y)) {
			this.x = nuevaX;
			this.imagenActual = imagenIzquierda;
		}
	}

	public void moverDerecha(Obstaculos[] rocas) {
		double nuevaX = this.x + velocidad;
		if (nuevaX < ANCHO_PANTALLA - ANCHO_MENU - ancho / 2 && !colisionaConRoca(rocas, nuevaX, this.y)) {
			this.x = nuevaX;
			this.imagenActual = imagenDerecha;
		}
	}

	public boolean colisionaConRoca(Obstaculos[] rocas, double nuevaX, double nuevaY) {
		for (Obstaculos roca : rocas) {
			if (roca != null) {
				boolean colisionX = nuevaX + ancho / 2 >= roca.getX() - roca.getAncho() / 2
						&& nuevaX - ancho / 2 <= roca.getX() + roca.getAncho() / 2;
				boolean colisionY = nuevaY + alto / 2 >= roca.getY() - roca.getAlto() / 2
						&& nuevaY - alto / 2 <= roca.getY() + roca.getAlto() / 2;
				if (colisionX && colisionY)
					return true;
			}
		}
		return false;
	}

	public void dibujar(Entorno entorno) {
		entorno.dibujarImagen(imagenActual, x, y, 0);
	}

	public boolean colisionaEnemigo(Murcielago[] murcielagos) {
		for (int i = 0; i < murcielagos.length; i++) {
			Murcielago m = murcielagos[i];
			if (m != null && this.y + this.alto / 2 - 25 <= m.getY() + m.getAlto() / 2
					&& this.y - this.alto / 2 + 30 >= m.getY() - m.getAlto() / 2
					&& this.x - this.ancho / 2 + 10 <= m.getX() + m.getAncho() / 2
					&& this.x + this.ancho / 2 - 10 >= m.getX() - m.getAncho() / 2) {

				this.vida -= 10; // o la cantidad que quieras restar
				murcielagos[i] = null; // Elimina el murciélago que chocó
				this.sumarMana(5);
				return true;
			}
		}
		return false;
	}

	public void restarVida(int cantidad) {
		vida -= cantidad;
		if (vida < 0) {
			vida = 0;
		}
	}

	public void sumarMana(int cantidad) {
		mana += cantidad;
		if (mana > 100) {
			mana = 100;
		}
		if (mana < 0) {
			mana = 0;
		}
	}

	public void restarMana(int cantidad) {
		mana -= cantidad;
		{
			if (mana < 0) { // Límite inferior (opcional)
				mana = 0;
			}
		}
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getVida() {
		return vida;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}
}
