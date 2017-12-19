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
import static org.lorislab.clingo4j.api.Clingo.LIB;

/**
 *
 * @author andrej
 */
public class Signature {

    private long signature;

    public long getSignature() {
        return signature;
    }

    public String getName() {
        Pointer<Byte> tmp = LIB.clingo_signature_name(signature);
        return tmp.getCString();
    }

    public int getArity() {
        return LIB.clingo_signature_arity(signature);
    }

    public boolean isPositive() {
        return LIB.clingo_signature_is_positive(signature);
    }

    public boolean isNegative() {
        return LIB.clingo_signature_is_negative(signature);
    }

    @Override
    public String toString() {
        return (isNegative() ? "-" : "") + getName() + "/" + getArity();
    }

}
