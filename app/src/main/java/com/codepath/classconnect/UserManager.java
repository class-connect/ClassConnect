package com.codepath.classconnect;

import com.codepath.classconnect.models.AppUser;
import com.facebook.Profile;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by ssunda1 on 6/22/16.
 */
public class UserManager {

    private static AppUser currentUser;

    private UserManager() {

    }

    public static AppUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser() {
        final Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            final String id = profile.getId();
            AppUser.findByUserId(id, new FindCallback<AppUser>() {
                @Override
                public void done(List<AppUser> objects, ParseException e) {
                    if (e == null) {
                        if (objects != null && !objects.isEmpty()) {
                            AppUser existingUser = objects.get(0);
                            existingUser.setName(profile.getName());
                            existingUser.setProfileUrl(profile.getProfilePictureUri(140, 140).toString());
                            existingUser.saveInBackground();
                            currentUser = existingUser;
                        }
                        else {
                            final AppUser newUser = new AppUser();
                            newUser.setUserId(id);
                            newUser.setName(profile.getName());
                            newUser.setProfileUrl(profile.getProfilePictureUri(140, 140).toString());
                            newUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        currentUser = newUser;
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public static void refreshCurrentUser() {
        if (currentUser != null) {
            AppUser.findByObjectId(currentUser.getObjectId(), new GetCallback<AppUser>() {
                @Override
                public void done(AppUser object, ParseException e) {
                    currentUser = object;
                }
            });
        }
    }

}
