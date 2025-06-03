package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Murcielago {
	private double x;
	private double y;
	private double ancho;
	private double alto;
	private String salida; // "arriba", "abajo", "izquierda", "derecha"
	private boolean activo;
	private Image imagenMurcielago;

	public Murcielago(double x, double y, int ancho, int alto, String salida) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.salida = salida;
		this.activo = false;
		this.imagenMurcielago = Herramientas.cargarImagen("Imagenes TP Programacion 1/Murcielago 2.gif");
	}

	public void activar() {
		this.activo = true;
	}

	public boolean estaActivo() {
		return activo;
	}

	public void desactivar() {
		this.activo = false;
	}

	public void dibujar(Entorno entorno) {
		if (activo) {
			entorno.dibujarImagen(imagenMurcielago, x, y, 0.2, 0.2);
		}
	}

	public void moverHacia(double objetivoX, double objetivoY) {
		if (!activo)
			return;
		double dx = objetivoX - this.x;
		double dy = objetivoY - this.y;
		double distancia = Math.sqrt(dx * dx + dy * dy);

		if (distancia > 0) {
			this.x += 2 * dx / distancia;
			this.y += 2 * dy / distancia;
		}
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
