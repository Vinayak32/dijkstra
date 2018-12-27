package com.example.domain;

public class Edge {
	public PlanetVertex end;
	public float distance;

	@Override
	public String toString() {
		return "Edge [end=" + end + ", distance=" + distance + "]";
	}

	public Edge(PlanetVertex end, float distance) {
		this.end = end;
		this.distance = distance;
	}
}
