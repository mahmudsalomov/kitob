package com.android.kitob.repository;

import com.android.kitob.model.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileStorageRepository extends JpaRepository<FileStorage,Integer> {
    FileStorage findByHashId(String hashId);
    FileStorage findByName(String name);
}
