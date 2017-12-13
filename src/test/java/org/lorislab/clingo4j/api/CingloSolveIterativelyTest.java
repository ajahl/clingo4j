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

import java.util.Iterator;
import org.junit.Test;

/**
 *
 * @author andrej
 */
public class CingloSolveIterativelyTest {
    
    private static final String PROGRAM = "#const n = 10.\n"
            + "n(1..n).\n"
            + "\n"
            + "q(X,Y) :- n(X), n(Y), not not q(X,Y).\n"
            + "\n"
            + "        c(r,X; c,Y) :- q(X,Y).\n"
            + "not not c(r,N; c,N) :- n(N).\n"
            + "\n"
            + "n(r,X,Y-1,X,Y; c,X-1,Y,X,Y; d1,X-1,Y-1,X,Y;     d2,X-1,Y+1,X,Y      ) :- n(X), n(Y).\n"
            + "c(r,N,0;       c,0,N;       d1,N-1,0; d1,0,N-1; d2,N-1,n+1; d2,0,N+1) :- n(N).\n"
            + "\n"
            + "c(C,XX,YY) :-     c(C,X,Y), n(C,X,Y,XX,YY), not q(XX,YY).\n"
            + "           :- not c(C,X,Y), n(C,X,Y,XX,YY),     q(XX,YY).\n"
            + "\n"
            + "#show q/2.";
    
    @Test
    public void controlTest() {
        
        Clingo.init("src/main/clingo");
        
        try (Clingo control = new Clingo()) {
            
            System.out.println(control.getVersion());
            
            control.add("base", PROGRAM);
            control.ground("base");
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
