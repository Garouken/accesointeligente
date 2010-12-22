package org.accesointeligente.shared;

public enum RequestStatus {
	NEW("Nueva"),
	PENDING("Pendiente"),
	CLOSED("Cerrada");

	private String name;

	private RequestStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}