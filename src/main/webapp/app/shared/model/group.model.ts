export interface IGroup {
    id?: number;
    groupName?: string;
    groupCode?: string;
    groupOwner?: string;
    groupCategoryId?: string;
}

export class Group implements IGroup {
    constructor(
        public id?: number,
        public groupName?: string,
        public groupCode?: string,
        public groupOwner?: string,
        public groupCategoryId?: string
    ) {}
}
