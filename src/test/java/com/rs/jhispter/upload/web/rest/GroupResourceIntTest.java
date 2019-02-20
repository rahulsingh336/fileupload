package com.rs.jhispter.upload.web.rest;

import com.rs.jhispter.upload.FileUploadApp;

import com.rs.jhispter.upload.domain.Group;
import com.rs.jhispter.upload.repository.GroupRepository;
import com.rs.jhispter.upload.service.GroupService;
import com.rs.jhispter.upload.service.dto.GroupDTO;
import com.rs.jhispter.upload.service.mapper.GroupMapper;
import com.rs.jhispter.upload.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.rs.jhispter.upload.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GroupResource REST controller.
 *
 * @see GroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FileUploadApp.class)
public class GroupResourceIntTest {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_CATEGORY_ID = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_CATEGORY_ID = "BBBBBBBBBB";

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupService groupService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restGroupMockMvc;

    private Group group;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GroupResource groupResource = new GroupResource(groupService);
        this.restGroupMockMvc = MockMvcBuilders.standaloneSetup(groupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createEntity(EntityManager em) {
        Group group = new Group()
            .groupName(DEFAULT_GROUP_NAME)
            .groupCode(DEFAULT_GROUP_CODE)
            .groupOwner(DEFAULT_GROUP_OWNER)
            .groupCategoryId(DEFAULT_GROUP_CATEGORY_ID);
        return group;
    }

    @Before
    public void initTest() {
        group = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroup() throws Exception {
        int databaseSizeBeforeCreate = groupRepository.findAll().size();

        // Create the Group
        GroupDTO groupDTO = groupMapper.toDto(group);
        restGroupMockMvc.perform(post("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupDTO)))
            .andExpect(status().isCreated());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeCreate + 1);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testGroup.getGroupCode()).isEqualTo(DEFAULT_GROUP_CODE);
        assertThat(testGroup.getGroupOwner()).isEqualTo(DEFAULT_GROUP_OWNER);
        assertThat(testGroup.getGroupCategoryId()).isEqualTo(DEFAULT_GROUP_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void createGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupRepository.findAll().size();

        // Create the Group with an existing ID
        group.setId(1L);
        GroupDTO groupDTO = groupMapper.toDto(group);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupMockMvc.perform(post("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGroups() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList
        restGroupMockMvc.perform(get("/api/groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(group.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].groupCode").value(hasItem(DEFAULT_GROUP_CODE.toString())))
            .andExpect(jsonPath("$.[*].groupOwner").value(hasItem(DEFAULT_GROUP_OWNER.toString())))
            .andExpect(jsonPath("$.[*].groupCategoryId").value(hasItem(DEFAULT_GROUP_CATEGORY_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get the group
        restGroupMockMvc.perform(get("/api/groups/{id}", group.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(group.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME.toString()))
            .andExpect(jsonPath("$.groupCode").value(DEFAULT_GROUP_CODE.toString()))
            .andExpect(jsonPath("$.groupOwner").value(DEFAULT_GROUP_OWNER.toString()))
            .andExpect(jsonPath("$.groupCategoryId").value(DEFAULT_GROUP_CATEGORY_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGroup() throws Exception {
        // Get the group
        restGroupMockMvc.perform(get("/api/groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // Update the group
        Group updatedGroup = groupRepository.findById(group.getId()).get();
        // Disconnect from session so that the updates on updatedGroup are not directly saved in db
        em.detach(updatedGroup);
        updatedGroup
            .groupName(UPDATED_GROUP_NAME)
            .groupCode(UPDATED_GROUP_CODE)
            .groupOwner(UPDATED_GROUP_OWNER)
            .groupCategoryId(UPDATED_GROUP_CATEGORY_ID);
        GroupDTO groupDTO = groupMapper.toDto(updatedGroup);

        restGroupMockMvc.perform(put("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupDTO)))
            .andExpect(status().isOk());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testGroup.getGroupCode()).isEqualTo(UPDATED_GROUP_CODE);
        assertThat(testGroup.getGroupOwner()).isEqualTo(UPDATED_GROUP_OWNER);
        assertThat(testGroup.getGroupCategoryId()).isEqualTo(UPDATED_GROUP_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // Create the Group
        GroupDTO groupDTO = groupMapper.toDto(group);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMockMvc.perform(put("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        int databaseSizeBeforeDelete = groupRepository.findAll().size();

        // Delete the group
        restGroupMockMvc.perform(delete("/api/groups/{id}", group.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Group.class);
        Group group1 = new Group();
        group1.setId(1L);
        Group group2 = new Group();
        group2.setId(group1.getId());
        assertThat(group1).isEqualTo(group2);
        group2.setId(2L);
        assertThat(group1).isNotEqualTo(group2);
        group1.setId(null);
        assertThat(group1).isNotEqualTo(group2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupDTO.class);
        GroupDTO groupDTO1 = new GroupDTO();
        groupDTO1.setId(1L);
        GroupDTO groupDTO2 = new GroupDTO();
        assertThat(groupDTO1).isNotEqualTo(groupDTO2);
        groupDTO2.setId(groupDTO1.getId());
        assertThat(groupDTO1).isEqualTo(groupDTO2);
        groupDTO2.setId(2L);
        assertThat(groupDTO1).isNotEqualTo(groupDTO2);
        groupDTO1.setId(null);
        assertThat(groupDTO1).isNotEqualTo(groupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(groupMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(groupMapper.fromId(null)).isNull();
    }
}
