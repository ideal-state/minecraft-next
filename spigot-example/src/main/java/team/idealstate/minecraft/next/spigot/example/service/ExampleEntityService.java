/*
 *    minecraft-next
 *    Copyright (C) 2025  ideal-state
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package team.idealstate.minecraft.next.spigot.example.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import team.idealstate.minecraft.next.spigot.example.data.ExampleEntity;
import team.idealstate.minecraft.next.spigot.example.mapper.ExampleEntityMapper;
import team.idealstate.sugar.logging.Log;
import team.idealstate.sugar.next.boot.mybatis.MyBatis;
import team.idealstate.sugar.next.context.annotation.component.Service;
import team.idealstate.sugar.next.context.annotation.feature.Autowired;
import team.idealstate.sugar.next.context.annotation.feature.Environment;
import team.idealstate.sugar.next.context.annotation.feature.Scope;
import team.idealstate.sugar.next.context.lifecycle.Initializable;
import team.idealstate.sugar.next.database.annotation.Transaction;
import team.idealstate.sugar.next.uuid.UUIDUtils;
import team.idealstate.sugar.validate.Validation;
import team.idealstate.sugar.validate.annotation.NotNull;

@Service
@Environment("development")
@Scope(Scope.SINGLETON)
public class ExampleEntityService implements Initializable {

    private static final Set<String> NAMES =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee", "fff")));

    @Transaction
    public String ping() {
        String tableName = ExampleEntity.class.getSimpleName();
        ExampleEntityMapper mapper = getMapper();
        mapper.selectAll().stream().map(String::valueOf).forEach(Log::info);
        ExampleEntity record = new ExampleEntity();
        for (String name : NAMES) {
            UUID uuid = UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8));
            byte[] uniqueId = UUIDUtils.uuidToBinary(uuid);
            record.setUniqueId(uniqueId);
            ExampleEntity selected = mapper.selectOne(record);
            if (selected == null) {
                Log.warn(String.format("[%s][%s] No found record in table: %s.", name, uuid, tableName));
            } else {
                Log.info(String.format("[%s][%s] Record found in table: %s. %s", name, uuid, tableName, selected));
            }
            record.setUniqueId(null);
            record.setName(null);
            record.setCreatedAt(null);
            record.setUpdatedAt(null);
        }
        return mapper.ping();
    }

    private volatile ExampleEntityMapper mapper;

    @Autowired
    public void setMapper(@NotNull ExampleEntityMapper mapper) {
        this.mapper = mapper;
    }

    @NotNull
    private ExampleEntityMapper getMapper() {
        return Validation.requireNotNull(mapper, "Mapper must not be null.");
    }

    @Override
    @Transaction(executionMode = MyBatis.EXECUTION_MODE_BATCH, isolationLevel = MyBatis.ISOLATION_LEVEL_SERIALIZABLE)
    public void initialize() {
        String tableName = ExampleEntity.class.getSimpleName();
        ExampleEntityMapper mapper = getMapper();
        mapper.createTable();
        ExampleEntity record = new ExampleEntity();
        for (String name : NAMES) {
            UUID uuid = UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8));
            byte[] uniqueId = UUIDUtils.uuidToBinary(uuid);
            record.setUniqueId(uniqueId);
            boolean exists = mapper.exists(record);
            if (exists) {
                Log.info(String.format("[%s][%s] Record already exists in table: %s.", name, uuid, tableName));
                continue;
            }
            record.setUniqueId(uniqueId);
            record.setName(name);
            mapper.insert(record);
            Log.info(String.format("[%s][%s] Inserted 1 record(s) into table: %s.", name, uuid, tableName));
            record.setUniqueId(null);
            record.setName(null);
            record.setCreatedAt(null);
            record.setUpdatedAt(null);
        }
    }
}
