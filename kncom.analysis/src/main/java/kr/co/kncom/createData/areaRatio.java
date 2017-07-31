package kr.co.kncom.createData;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class areaRatio extends SimpleFileVisitor<Path> {

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		//System.out.println("## preVisitDirectory ==> " + dir);
		return super.preVisitDirectory(dir, attrs);
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		System.out.println("## visitFile ==> " + file);
		
		if (file.getFileName().equals("daejang_pyojebu.dat")) {
			// 여기서 서비스 호출
			
		}
		
		return super.visitFile(file, attrs);
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		//System.out.println("## visitFileFailed ==> " + file);
		return super.visitFileFailed(file, exc);
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		//System.out.println("## postVisitDirectory ==> " + dir);
		return super.postVisitDirectory(dir, exc);
	}
	
	public static void main(String[] args) {
	    Path path = Paths.get("F:\\201704lobig");
	    areaRatio mySimpleFileVisitor = new areaRatio();
	     
	    try {
	        Files.walkFileTree(path, mySimpleFileVisitor);
	    } catch (IOException ex) {
	    }
	}
	
}
