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
@Name("clingo_ast_disjoint_element") 
@Library("clingo") 
public class clingo_ast_disjoint_element extends StructObject {
	/** C type : clingo_location_t */
	@Field(0) 
	public clingo_location location() {
		return this.io.getNativeObjectField(this, 0);
	}
	/** C type : clingo_location_t */
	@Field(0) 
	public clingo_ast_disjoint_element location(clingo_location location) {
		this.io.setNativeObjectField(this, 0, location);
		return this;
	}
	/** C type : const clingo_ast_term_t* */
	@Field(1) 
	public Pointer<clingo_ast_term > tuple() {
		return this.io.getPointerField(this, 1);
	}
	/** C type : const clingo_ast_term_t* */
	@Field(1) 
	public clingo_ast_disjoint_element tuple(Pointer<clingo_ast_term > tuple) {
		this.io.setPointerField(this, 1, tuple);
		return this;
	}
	@Ptr 
	@Field(2) 
	public long tuple_size() {
		return this.io.getSizeTField(this, 2);
	}
	@Ptr 
	@Field(2) 
	public clingo_ast_disjoint_element tuple_size(long tuple_size) {
		this.io.setSizeTField(this, 2, tuple_size);
		return this;
	}
	/** C type : clingo_ast_csp_sum_term_t */
	@Field(3) 
	public clingo_ast_csp_sum_term term() {
		return this.io.getNativeObjectField(this, 3);
	}
	/** C type : clingo_ast_csp_sum_term_t */
	@Field(3) 
	public clingo_ast_disjoint_element term(clingo_ast_csp_sum_term term) {
		this.io.setNativeObjectField(this, 3, term);
		return this;
	}
	/** C type : const clingo_ast_literal_t* */
	@Field(4) 
	public Pointer<clingo_ast_literal > condition() {
		return this.io.getPointerField(this, 4);
	}
	/** C type : const clingo_ast_literal_t* */
	@Field(4) 
	public clingo_ast_disjoint_element condition(Pointer<clingo_ast_literal > condition) {
		this.io.setPointerField(this, 4, condition);
		return this;
	}
	@Ptr 
	@Field(5) 
	public long condition_size() {
		return this.io.getSizeTField(this, 5);
	}
	@Ptr 
	@Field(5) 
	public clingo_ast_disjoint_element condition_size(long condition_size) {
		this.io.setSizeTField(this, 5, condition_size);
		return this;
	}
	public clingo_ast_disjoint_element() {
		super();
	}
	public clingo_ast_disjoint_element(Pointer pointer) {
		super(pointer);
	}
}