package org.lorislab.clingo4j.c.api;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
import org.bridj.ann.Name;
import org.bridj.ann.Ptr;
/**
 * <i>native declaration : src/main/clingo/lib/c/clingo.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Name("clingo_ast_theory_unparsed_term_element") 
@Library("clingo") 
public class clingo_ast_theory_unparsed_term_element extends StructObject {
	/** C type : const char** */
	@Field(0) 
	public Pointer<Pointer<Byte > > operators() {
		return this.io.getPointerField(this, 0);
	}
	/** C type : const char** */
	@Field(0) 
	public clingo_ast_theory_unparsed_term_element operators(Pointer<Pointer<Byte > > operators) {
		this.io.setPointerField(this, 0, operators);
		return this;
	}
	@Ptr 
	@Field(1) 
	public long size() {
		return this.io.getSizeTField(this, 1);
	}
	@Ptr 
	@Field(1) 
	public clingo_ast_theory_unparsed_term_element size(long size) {
		this.io.setSizeTField(this, 1, size);
		return this;
	}
	/** C type : clingo_ast_theory_term_t */
	@Field(2) 
	public clingo_ast_theory_term term() {
		return this.io.getNativeObjectField(this, 2);
	}
	/** C type : clingo_ast_theory_term_t */
	@Field(2) 
	public clingo_ast_theory_unparsed_term_element term(clingo_ast_theory_term term) {
		this.io.setNativeObjectField(this, 2, term);
		return this;
	}
	public clingo_ast_theory_unparsed_term_element() {
		super();
	}
	public clingo_ast_theory_unparsed_term_element(Pointer pointer) {
		super(pointer);
	}
}