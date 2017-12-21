package org.lorislab.clingo4j.api.c;
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
@Name("clingo_ast_theory_atom") 
@Library("clingo") 
public class clingo_ast_theory_atom extends StructObject {
	/** C type : clingo_ast_term_t */
	@Field(0) 
	public clingo_ast_term term() {
		return this.io.getNativeObjectField(this, 0);
	}
	/** C type : clingo_ast_term_t */
	@Field(0) 
	public clingo_ast_theory_atom term(clingo_ast_term term) {
		this.io.setNativeObjectField(this, 0, term);
		return this;
	}
	/** C type : const clingo_ast_theory_atom_element_t* */
	@Field(1) 
	public Pointer<clingo_ast_theory_atom_element > elements() {
		return this.io.getPointerField(this, 1);
	}
	/** C type : const clingo_ast_theory_atom_element_t* */
	@Field(1) 
	public clingo_ast_theory_atom elements(Pointer<clingo_ast_theory_atom_element > elements) {
		this.io.setPointerField(this, 1, elements);
		return this;
	}
	@Ptr 
	@Field(2) 
	public long size() {
		return this.io.getSizeTField(this, 2);
	}
	@Ptr 
	@Field(2) 
	public clingo_ast_theory_atom size(long size) {
		this.io.setSizeTField(this, 2, size);
		return this;
	}
	/** C type : const clingo_ast_theory_guard_t* */
	@Field(3) 
	public Pointer<clingo_ast_theory_guard > guard() {
		return this.io.getPointerField(this, 3);
	}
	/** C type : const clingo_ast_theory_guard_t* */
	@Field(3) 
	public clingo_ast_theory_atom guard(Pointer<clingo_ast_theory_guard > guard) {
		this.io.setPointerField(this, 3, guard);
		return this;
	}
	public clingo_ast_theory_atom() {
		super();
	}
	public clingo_ast_theory_atom(Pointer pointer) {
		super(pointer);
	}
}