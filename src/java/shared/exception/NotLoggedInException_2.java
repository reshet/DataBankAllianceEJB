/** 
 * Copyright 2010 Daniel Guermeur and Amy Unruh
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   See http://connectrapp.appspot.com/ for a demo, and links to more information 
 *   about this app and the book that it accompanies.
 */
package shared.exception;

import java.io.Serializable;


@SuppressWarnings("serial")
public class NotLoggedInException_2 extends Exception implements Serializable{
  
  public NotLoggedInException_2() {
  }

  public NotLoggedInException_2(String message) {
    super(message);
  }

  public NotLoggedInException_2(String message, Throwable cause) {
    super(message, cause);
  }

  public NotLoggedInException_2(Throwable cause) {
    super(cause);
  }

}