/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.danieltaeschler.udemy.instagramclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;


public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId("42949aab31f2308049f26f3f65b7b75f4df48410")
            .clientKey("9726b9c1143c3c1a68d90d6a9a721b923aa0af83")
            .server("http://18.188.61.240:80/parse/")
            .build()
    );

    //ParseUser.enableAutomaticUser();

    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);

  }
}
