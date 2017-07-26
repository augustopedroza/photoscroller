package com.pedroza.photoscroller.photoscroller;

import com.pedroza.photoscroller.photoscroller.model.response.Photo.Photo;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by pedroza on 7/26/17.
 */

public class PhotoUnitTest {
    @Test
    public void photo_setid() throws Exception {
        Photo photo = new Photo();
        photo.setId("100");
        Assert.assertEquals("100", photo.getId());
    }
}
