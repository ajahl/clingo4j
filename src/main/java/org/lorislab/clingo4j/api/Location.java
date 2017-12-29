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

import org.bridj.Pointer;
import org.lorislab.clingo4j.api.c.clingo_location;

/**
 *
 * @author andrej
 */
public class Location extends clingo_location {

    public Location(clingo_location loc) {
        super(Pointer.getPointer(loc));
    }
    
    public Location(Pointer<clingo_location> location) {
        super(location);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Location(String beginFile, String endFile, long beginLine, long endLine, long beginColumn, long endColumn) {
        begin_file(Pointer.pointerToCString(beginFile));
        end_file(Pointer.pointerToCString(endFile));
        begin_line(beginLine);
        end_line(endLine);
        begin_column(beginColumn);
        end_column(endColumn);
    }

    public long getBeginColumn() {
        return this.begin_column();
    }

    public String getBeginFile() {
        return this.begin_file().getCString();
    }

    public long getBeginLine() {
        return this.begin_line();
    }

    public long getEndColumn() {
        return this.end_column();
    }

    public String getEndFile() {
        return this.end_file().getCString();
    }

    public long getEndLine() {
        return this.end_line();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getBeginFile()).append(":").append(getBeginLine()).append(":").append(getBeginColumn());

        boolean dash = true;
        boolean eq = getBeginFile().equals(getEndFile());
        if (!eq) {
            if (dash) {
                sb.append("-");
            } else {
                sb.append(":");
            }
            sb.append(getEndFile());
            dash = false;
        }
        eq = eq && (getBeginLine() == getEndLine());
        if (!eq) {
            if (dash) {
                sb.append("-");
            } else {
                sb.append(":");
            }
            sb.append(getEndLine());
            dash = false;
        }
        eq = eq && (getBeginColumn() == getEndColumn());
        if (!eq) {
            if (dash) {
                sb.append("-");
            } else {
                sb.append(":");
            }
            sb.append(getEndColumn());
        }
        return sb.toString();
    }

}
