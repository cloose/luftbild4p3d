/*
 * Copyright 2013 - 2016 Mario Arias
 * Copyright 2017        Christian Loose
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.luftbild4p3d.base.composition

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec

class CompositionTest : StringSpec ({

    val add5 = { i: Int -> i + 5 }
    val multiplyBy2 = { i: Int -> i * 2 }

    "andThen combines two functions left-to-right using the result of the left function as parameter of the right function" {
        val add5ThenMultiplyBy2 = add5 andThen multiplyBy2

        add5ThenMultiplyBy2(2) shouldEqual 14
    }

    "compose combines two functions right-to-left using the result of the right function as parameter of the left function" {
        val multiplyBy2ThenAdd5 = add5 compose multiplyBy2

        multiplyBy2ThenAdd5(2) shouldEqual 9
    }

})
