package no.kantega.forum.control;

import no.kantega.publishing.common.data.ImageResizeParameters;
import no.kantega.publishing.common.data.Multimedia;
import no.kantega.publishing.common.exception.InvalidImageFormatException;
import no.kantega.publishing.multimedia.ImageEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MultimediaRequestHandlerHelper {
    private static final Logger log = LoggerFactory.getLogger(no.kantega.publishing.client.MultimediaRequestHandlerHelper.class);

    private ImageEditor imageEditor;

    @Cacheable(value = "ImageCache", key = "#calculatedKey")
    public byte[] getResizedMultimediaBytes(String calculatedKey, Multimedia mm, ImageResizeParameters resizeParams) throws IOException, InvalidImageFormatException {
        log.debug( "Resizing image with key: {}", calculatedKey);

        mm = imageEditor.resizeMultimedia(mm, resizeParams);

        return mm.getData();
    }

    public void setImageEditor(ImageEditor imageEditor) {
        this.imageEditor = imageEditor;
    }
}
