package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Botonera {
	private double x;
	private double y;
	private double ancho;
	private double alto;
	private Image explosion1;
	private Image explosion2;
	private Image explosion3;
	private double radio = 60; // valor ajustable

	public Botonera(double x, double y) {
		this.x = x;
		this.y = y;
		this.ancho = 10;
		this.alto = 10;

		this.explosion1 = Herramientas.cargarImagen("Imagenes TP Programacion 1/Explosion 1 (fuego).gif")
				.getScaledInstance(500, 500, 0);
		this.explosion2 = Herramientas.cargarImagen("Imagenes TP Programacion 1/Explosion 2 (Agua).gif")
				.getScaledInstance(750, 750, 0);
		this.explosion3 = Herramientas.cargarImagen("Imagenes TP Programacion 1/Explosion 3 (Veneno).gif")
				.getScaledInstance(1500, 1500, 0);
	}

	public void dibujarExplosion1(Entorno entorno) {
		entorno.dibujarImagen(this.explosion1, this.x, this.y, 0, this.ancho / 100.0);
	}

	public void dibujarExplosion2(Entorno entorno) {
		entorno.dibujarImagen(this.explosion2, this.x, this.y, 0, this.ancho / 100.0);
	}

	public void dibujarExplosion3(Entorno entorno) {
		entorno.dibujarImagen(this.explosion3, this.x, this.y, 0, this.ancho / 100.0);
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

	public double getAncho() {
		return ancho;
	}

	public void setAncho(double ancho) {
		this.ancho = ancho;
	}

	public double getAlto() {
		return alto;
	}

	public void setAlto(double alto) {
		this.alto = alto;
	}

	public double getRadio() {
		return radio;
	}

}
	


