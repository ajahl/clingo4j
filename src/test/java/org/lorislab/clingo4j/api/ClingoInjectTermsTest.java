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
package org.lorislab.clingo4j.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author andrej
 */
public class ClingoInjectTermsTest {
    
    @Test
    public void controlTest() {
        
        Clingo.init("src/main/clingo");
        
        try (Clingo control = new Clingo()) {
            
            // define a constant in string form
            Symbol number = Clingo.createNumber(23);
            control.add("base", "#const d=" + number);
            
        // define a constant via the AST
//        ctl.with_builder([](ProgramBuilder &b) {
//            Location loc{"<generated>", "<generated>", 1, 1, 1, 1};
//            b.add({loc, AST::Definition{"e", {loc, Number(24)}, false}});
//        });
        
            control.add("base", "p(@c()). p(d). p(e).");            
            // inject terms via a callback
            control.ground("base", (Location loc, String name, List<Symbol> symbols, GroundCallback.GroundSymbolCallback callback) -> {
                if ("c".equals(name) && ( symbols == null || symbols.isEmpty())) {                    
                    List<Symbol> tmp = new ArrayList<>(2);
                    tmp.add(Clingo.createNumber(42));
                    tmp.add(Clingo.createNumber(43));
                    callback.apply(tmp);
                }
            });
            
            Iterator<Model> iter = control.solve();
            while (iter.hasNext()) {
                Model model = iter.next();
                System.out.println("Model type: " + model.getType());
                for (Symbol atom : model.getSymbols()) {
                    System.out.println(atom);
                }
            }
        
        } catch (ClingoException ex) {
           System.err.println(ex.getMessage());
        }        
    }    
}
