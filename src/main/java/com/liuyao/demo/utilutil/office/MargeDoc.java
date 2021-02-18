package com.liuyao.demo.utilutil.office;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;


/**
 * word 文档合并
 */
public class MargeDoc {

    public void mergeDocx(List<String> list, String path) {
        List<InputStream> inList = new ArrayList<InputStream>();
        for (int i = 0; i < list.size(); i++)
            try {
                inList.add(new FileInputStream(list.get(i)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        try {
            InputStream inputStream = mergeDocx(inList);
            saveTemplate(inputStream, path);
        } catch (Docx4JException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public InputStream mergeDocx(final List<InputStream> streams) throws Docx4JException, IOException {

        WordprocessingMLPackage target = null;
        final File generated = File.createTempFile("generated", ".docx");

        int chunkId = 0;
        Iterator<InputStream> it = streams.iterator();
        while (it.hasNext()) {
            InputStream is = it.next();
            if (is != null) {
                if (target == null) {
                    // Copy first (master) document
                    OutputStream os = new FileOutputStream(generated);
                    os.write(IOUtils.toByteArray(is));
                    os.close();

                    target = WordprocessingMLPackage.load(generated);
                } else {
                    // Attach the others (Alternative input parts)
                    insertDocx(target.getMainDocumentPart(), IOUtils.toByteArray(is), chunkId++);
                }
            }
        }

        if (target != null) {
            target.save(generated);
            return new FileInputStream(generated);
        } else {
            return null;
        }
    }

    // 插入文档
    private void insertDocx(MainDocumentPart main, byte[] bytes, int chunkId) {
        try {
            AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(
                    new PartName("/part" + chunkId + ".docx"));
            // afiPart.setContentType(new ContentType(CONTENT_TYPE));
            afiPart.setBinaryData(bytes);
            Relationship altChunkRel = main.addTargetPart(afiPart);

            CTAltChunk chunk = Context.getWmlObjectFactory().createCTAltChunk();
            chunk.setId(altChunkRel.getId());

            main.addObject(chunk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveTemplate(InputStream fis, String toDocPath) {
        FileOutputStream fos;
        int bytesum = 0;
        int byteread = 0;
        try {
            fos = new FileOutputStream(toDocPath);
            byte[] buffer = new byte[1444];
            while ((byteread = fis.read(buffer)) != -1) {
                bytesum += byteread; // 字节数 文件大小
                fos.write(buffer, 0, byteread);
            }
            fis.close();
            fos.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Docx4JException, IOException{
        MargeDoc wordUtil=new MargeDoc();
        String template="C:\\Users\\61\\Desktop\\doc";
        List<String> list=new ArrayList<String>();
        list.add(template+"/t.docx");
        list.add(template+"/1.docx");
        list.add(template+"/2.docx");
        list.add(template+"/3.docx");

        wordUtil.mergeDocx(list, template+"/out.docx");
    }
}
