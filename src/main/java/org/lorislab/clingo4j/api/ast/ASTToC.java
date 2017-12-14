/*
 * Copyright 2017 andrej.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lorislab.clingo4j.api.ast;

import org.bridj.Pointer;
import org.lorislab.clingo4j.api.Symbol;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_term_type;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_term_type.clingo_ast_term_type_binary_operation;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_term_type.clingo_ast_term_type_symbol;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_term_type.clingo_ast_term_type_unary_operation;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_term_type.clingo_ast_term_type_variable;
import org.lorislab.clingo4j.c.api.clingo_ast_binary_operation;
import org.lorislab.clingo4j.c.api.clingo_ast_id;
import org.lorislab.clingo4j.c.api.clingo_ast_term;
import org.lorislab.clingo4j.c.api.clingo_ast_unary_operation;

/**
 *
 * @author andrej
 */
public class ASTToC {
    
    static clingo_ast_id convId(final Id id) {        
        clingo_ast_id tmp = new clingo_ast_id();        
        tmp.id(Pointer.pointerToCString(id.getId()));
        tmp.location(id.getLocation());
        return tmp;
    }    
    
    private class TermTag {};
    
    private static clingo_ast_term createAstTerm(clingo_ast_term_type type) {
        clingo_ast_term ret = new clingo_ast_term();
        ret.type((int) type.value);
        return ret;             
    }
    
    static clingo_ast_term visit(final Symbol x, TermTag t) {
        clingo_ast_term ret = createAstTerm(clingo_ast_term_type_symbol);
        ret.field1().symbol(x.getPointer().get());
        return ret;
    }    
    
    static clingo_ast_term visit(final Variable x, TermTag t) {
        clingo_ast_term ret = createAstTerm(clingo_ast_term_type_variable);
        ret.field1().variable(Pointer.pointerToCString(x.getName()));
        return ret;
    }    
    
    static clingo_ast_term visit(final UnaryOperation x, TermTag t) {
        
        clingo_ast_unary_operation uo = new clingo_ast_unary_operation();
        uo.unary_operator(x.getUnaryOperator().getValue());
        uo.argument(convTerm(x.getArgument()));
        
        Pointer<clingo_ast_unary_operation> pointer = Pointer.getPointer(uo);
                
        clingo_ast_term ret = createAstTerm(clingo_ast_term_type_unary_operation);
        ret.field1().unary_operation(pointer);
        return ret;
    }    
    
    static clingo_ast_term visit(final BinaryOperation x, TermTag t) {
        
        clingo_ast_binary_operation bo = new clingo_ast_binary_operation();
        bo.binary_operator(x.getOperator().getValue());
        bo.left(convTerm(x.getLeft()));
        bo.right(convTerm(x.getRight()));
        
        Pointer<clingo_ast_binary_operation> pointer = Pointer.getPointer(bo);
        
        clingo_ast_term ret = createAstTerm(clingo_ast_term_type_binary_operation);
        ret.field1().binary_operation(pointer);
        return ret;
    }
    
    static clingo_ast_term convTerm(Term x) {
        // TODO: missing implementation
//        clingo_ast_term ret = x.data.accept(*this, new TermTag());
//        ret.location(x.getLocation());
//        return ret;
        return null;
    }    
}
