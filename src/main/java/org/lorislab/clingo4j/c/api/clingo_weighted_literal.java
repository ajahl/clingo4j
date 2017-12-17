package org.lorislab.clingo4j.c.api;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
import org.bridj.ann.Name;
/**
 * <i>native declaration : src/main/clingo/lib/c/clingo.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Name("clingo_weighted_literal") 
@Library("clingo") 
public class clingo_weighted_literal extends StructObject {
	/** C type : clingo_literal_t */
	@Field(0) 
	public int literal() {
		return this.io.getIntField(this, 0);
	}
	/** C type : clingo_literal_t */
	@Field(0) 
	public clingo_weighted_literal literal(int literal) {
		this.io.setIntField(this, 0, literal);
		return this;
	}
	/** C type : clingo_weight_t */
	@Field(1) 
	public int weight() {
		return this.io.getIntField(this, 1);
	}
	/** C type : clingo_weight_t */
	@Field(1) 
	public clingo_weighted_literal weight(int weight) {
		this.io.setIntField(this, 1, weight);
		return this;
	}
	public clingo_weighted_literal() {
		super();
	}
	public clingo_weighted_literal(Pointer pointer) {
		super(pointer);
	}
}