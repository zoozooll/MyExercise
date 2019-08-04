using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Fallingdown : MonoBehaviour {

	Rigidbody rb;

	// Use this for initialization
	void Start () {
		rb = GetComponent<Rigidbody> ();
	}
	
	// Update is called once per frame
	void Update () {
		if (Input.GetKeyDown (KeyCode.A)) {
			rb.velocity = new Vector3 (0.0f, 2.0f, 0.0f);
		}
	}
}
