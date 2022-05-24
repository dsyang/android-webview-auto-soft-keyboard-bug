/*
 * Copyright (C) 2020 The Android Open Source Project
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
function focusToShowSoftKeyboard() {
    console.log("I am about to focus the following element: ", document.getElementById("focusme"));
	document.getElementById("focusme").focus();
	console.log("details", {
	  activeElement: document.activeElement,
	  hasFocus: document.hasFocus(),
	  focusMe: document.getElementById("focusme")
	})
}

function onPageFinished() {
    console.log("onPageFinished")
    setTimeout(() => {
      console.log("focusing to show keyboard")
      focusToShowSoftKeyboard()
    }, 1000)
}
