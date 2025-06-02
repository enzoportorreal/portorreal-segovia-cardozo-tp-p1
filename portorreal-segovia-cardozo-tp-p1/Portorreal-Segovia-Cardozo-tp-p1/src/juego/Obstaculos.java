package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Obstaculos {
	private double x;
	private double y;
	private double ancho;
	private double alto;
	private Image roca;

	public Obstaculos(double x, double y) {
		this.x = x;
		this.y = y;
		this.ancho = 40;
		this.alto = 40;
		this.roca = Herramientas.cargarImagen("Imagenes TP Programacion 1/roca.png");

	}

	public void dibujarRoca(Entorno entorno) {
		entorno.dibujarImagen(this.roca, this.x, this.y, 0, 0.1);
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
}
